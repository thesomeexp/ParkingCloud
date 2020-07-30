package cn.edu.lingnan.controller;

import ch.hsr.geohash.GeoHash;
import cn.edu.lingnan.dto.TempUserDTO;
import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.entity.Temp;
import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.service.InfoService;
import cn.edu.lingnan.service.UserClientService;
import cn.edu.lingnan.util.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * (Info)表控制层
 *
 * @author makejava
 * @since 2020-06-04 11:31:43
 */
@RestController
public class InfoController {

    @Resource
    private InfoService infoService;

    @Autowired
    private UserClientService userClientService;

    /**
     * 添加一个新的停车场
     * 1.
     * 2.从请求获取jwt然后得到当前用户
     * 3.检查名字, 位置, 图片是否为空
     * 4.检查名字, 内容是否超出
     * 5.判断位置是否合法, 是否存在
     * 6.不存在则添加记录
     * 7.添加首张图片
     *
     * @param name
     * @param location
     * @param image
     * @param content
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/infos")
    public Result addInfo(String name, String location, MultipartFile image,
                          String content, HttpServletRequest request) throws Exception {
        int id;
        int score;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            id = MyTool.getUserId(token);
            score = MyTool.getUserScore(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if (image == null || image.isEmpty())
            return Result.fail(MagicVariable.INFO_IMAGE_IS_EMPTY);
        if (MyTool.isStringEmpty(name))
            return Result.fail(MagicVariable.INFO_NAME_IS_EMPTY);
        if (MyTool.isStringEmpty(location))
            return Result.fail(MagicVariable.INFO_LOCATION_EMPTY);
        if (!MyTool.isValueLengthLegal(name, MagicVariable.INFO_NAME_MAX_LEN) ||
                !MyTool.isValueLengthLegal(content, MagicVariable.INFO_CONTENT_MAX_LEN))
            return Result.fail(MagicVariable.PARAM_VALUES_TOO_MAX);

        double[] xyArray = MyTool.praseLocation(location);
        double x = xyArray[0];
        double y = xyArray[1];
        if (!MyTool.isXYLegal(x, y))
            return Result.fail(MagicVariable.LOCATION_ILLEGAL);

        if (!infoService.isLocationExist(x, y).isEmpty())
            return Result.fail(MagicVariable.PARKING_IS_EXIST);
        Info newInfo = new Info();
        newInfo.setName(name);
        newInfo.setContent(content);
        newInfo.setLongitude(x);
        newInfo.setLatitude(y);
        newInfo.setGeohash(GeoHash.geoHashStringWithCharacterPrecision(y, x, MagicVariable.INFO_GEOHASH_LIMIT));
        newInfo.setUid(id);//用户登录的ID
        Date curr = new Date();
        newInfo.setInfosubmitdate(curr);
        newInfo.setStateupdatedate(curr);

        // 来了来了判断是否可以半自动
        File img_file = MyTool.multipartFileToFile(image);
        String[] res = new String[3];
        try {

            res = MyTool.readExif(img_file);
        } catch (NullPointerException e) {
            Result.fail("提交图片异常, 请更换一张图片");
        }

        boolean auto = true;
        if (res[0] == null || res[0].equals(""))
            auto = false;
        if (res[1] == null || res[1].equals(""))
            auto = false;
        if (res[2] == null || res[2].equals(""))
            auto = false;

        if (auto && score>500) {
            System.out.println("----LOG自动通过----");
            System.out.println("----------------------");
            System.out.println(res[0]);
            System.out.println(res[1]);
            System.out.println(res[2]);
            System.out.println("----------------------");
            // 判断坐标是否在附近
            double info_x = Double.valueOf(res[0]);
            double info_y = Double.valueOf(res[1]);
            String userGeoHash = GeoHash.geoHashStringWithCharacterPrecision(y, x,
                    MagicVariable.ADD_TEMP_GEOHASH_LIMIT);
            // 验证位置和图片位置
            GeoHash geoHash = GeoHash.withBitPrecision(info_x, info_y, MagicVariable.ADD_TEMP_GEOHASH_LIMIT*5);
            GeoHash[] arryGeoHash = geoHash.getAdjacent();
            if(!MyTool.isUserGeoHashInArray(userGeoHash, geoHash.toBase32(), arryGeoHash)){
                System.out.println("----LOG停车场坐标不在图片信息的定位附近-----");
                System.out.println("----LOG停车场坐标: " + x + ", " + y);
                System.out.println("----LOG图片坐标: " + info_y + ", " + info_x);
                System.out.println(userGeoHash);
                System.out.println(geoHash.toBase32());
                for (GeoHash h : arryGeoHash) {
                    System.out.println(h.toBase32());
                }

                newInfo.setState(MagicVariable.NO_VERIFIED);
            } else {
                // 判断照片拍摄时间是否在30分钟内
                Date image_date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                long image_time = format.parse(res[2]).getTime();
                long now = System.currentTimeMillis();
                long interval_time = MagicVariable.INTERVAL_TIME;
                if (image_time > now - interval_time) {
                    System.out.println("------LOG照片位置与提交位置相同且拍摄于30分钟内, 自动通过-----");

                    TempUserDTO tempUserDTO = new TempUserDTO();
                    tempUserDTO.setToUid(id);
                    tempUserDTO.setReason("停车位通过验证");
                    tempUserDTO.setScore(50);
                    Result add_score_result = this.userClientService.addScore(request.getHeader(JWTConfig.HEADER_TOKEN_KEY), tempUserDTO);
                    if ("success".equals(add_score_result.getStatus()))
                        System.out.println("--LOG: 添加用户积分成功 --");
                    else
                        System.out.println("-- LOG: 添加用户积分失败 --");
                    newInfo.setState(MagicVariable.VERIFIED);
                } else {
                    System.out.println("---LOG拍摄时间不在30分钟内");
                    System.out.println("拍摄时间: " + image_time);
                    System.out.println("当前时间: " + now);
                    newInfo.setState(MagicVariable.NO_VERIFIED);
                }
            }


        } else {
            System.out.println("不自动通过");
            newInfo.setState(MagicVariable.NO_VERIFIED);
        }
        infoService.setDefaultT(newInfo);
        infoService.insert(newInfo);
        Result result = saveOrUpdateImageFile(newInfo, image, request);// 添加首张图片
        if (result.getStatus().equals("success")) {
            return Result.success(MagicVariable.INFO_ADD_SUCCESS);
        } else {
            infoService.deleteById(newInfo.getId());
            return result;
        }
    }

    // TODO: 添加图片bug: 如果不是png, jpg文件可能会抛出异常, 后期可以做一下图片压缩

    /**
     * 由上面的函数调用 添加停车场首张图片
     *
     * @param bean
     * @param image
     * @param req
     * @return
     * @throws Exception
     */
    public Result saveOrUpdateImageFile(Info bean, MultipartFile image, HttpServletRequest req)
            throws Exception {
//        String filepath= System.getProperty("user.home");//指定图片上传到哪个文件夹的路径
//        File imageFolder = new File(filepath + "/image/info");
//        System.out.println(imageFolder);
//        File file = new File(imageFolder, bean.getId() + ".jpg");
//        System.out.println(file);
//        if (!file.getParentFile().exists())
//            file.getParentFile().mkdirs();
//        FileUtils.copyInputStreamToFile(image.getInputStream(), file);
//        BufferedImage img = ImageUtil.change2jpg(file);
//        System.out.println(img);
//        ImageIO.write(img, "jpg", file);
        InputStream inputStream;
        boolean result = false;
        try {
            inputStream = image.getInputStream();
            result = FtpUtil.uploadImage(inputStream, bean.getId() + ".jpg", "/home/nginx/www/image/");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("停车场首张图片上传的io异常,停车位添加失败");
        }
        if (result) {
            return Result.success(MagicVariable.INFO_ADD_SUCCESS);
        } else {
            return Result.fail("停车位添加失败");
        }
    }

    /**
     * 根据定位查询附近所有已认证的停车场
     *
     * @param location
     * @return
     */
    @GetMapping("/infos")
    public Result listNearbyInfos(String location) {
        try {
            if (MyTool.isStringEmpty(location))
                return Result.fail(MagicVariable.BAD_REQUEST);

            double[] xyArray = MyTool.praseLocation(location);
            double x = xyArray[0];
            double y = xyArray[1];
            if (!MyTool.isXYLegal(x, y))
                return Result.fail(MagicVariable.LOCATION_ILLEGAL);
            GeoHash geoHash = GeoHash.withBitPrecision(y, x, MagicVariable.SEARCH_GEOHASH_LIMIT * 5);
            GeoHash[] arryGeoHash = geoHash.getAdjacent();
            List<Info> infos = infoService.queryByGeoHashAndVerified(GeoHash.geoHashStringWithCharacterPrecision(y, x,
                    MagicVariable.SEARCH_GEOHASH_LIMIT));
            for (int i = 0; i < 8; i++) {
                infos.addAll(infoService.queryByGeoHashAndVerified(arryGeoHash[i].toBase32()));
            }
            Map<String, String> req_map = new HashMap<>();
            req_map.put("location", location);
            Map<String, Object> map = new HashMap<>();
            map.put("infos", infos);
            return Result.success(map, req_map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(MagicVariable.LOGGER_ERROR_NO_LOGIN);
        }
    }

    /**
     * 根据id查询停车场信息
     *
     * @param id
     * @return
     */
    @GetMapping("/infos/{id}")
    public Result get(@PathVariable("id") int id) {
        Info info = this.infoService.queryById(id);
        if (info == null) {
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        }
        Map<String, Object> data_map = new HashMap<>();
        data_map.put("info", info);
        return Result.success(data_map);
    }

    // TODO: 如果客户恶意掉用怎么处理?
    // TODO: feign 添加请求头判断用户是否有权限
    // TODO: SQL注入风险
    // TODO: Feign不能传递多个值, 所以还是在这边手动解析吧!!!!!!
    // TODO: HystrixCommand和全局错误处理的区别
    // FIXME: 存在漏洞, 通过客户端能直接访问修改, 考虑添加Redis使用随机UUID做访问安全控制
    // Temp调用更新T数据
    // , HttpServletRequest request
    @PutMapping("/infos")
//    @HystrixCommand(fallbackMethod = "processHystrix_Put")
    public Result put(@RequestBody Temp temp, HttpServletRequest request) {
        String token = MyTool.headerToToken(request.getHeader(JWTConfig.HEADER_TOKEN_KEY));
        boolean isValid = MyTool.isJWTAuthorized(token);
        if (!isValid)
            return Result.fail("JWT签名无效, 请重新登录");
        int t = LocalDateTime.now().getHour() - 1;
        if (t == -1)
            t = 23;
        int result = this.infoService.updateT(temp.getPid(), "t" + t, temp.getState());
        if (result == 1) {
            return Result.success();
        } else {
            return Result.fail("更新停车场状态信息失败");
        }
    }
    // 熔断方法
//    public Temp processHystrix_Put(@RequestBody Temp temp)
//    {
//        System.out.println("InfoControllerApi触发熔断/又或者你在尝试sql注入?(应该不会的)");
//        return null;
//    }


    /**
     * 查询该登录用户所提交的停车场
     *
     * @param page
     * @param pagesize
     * @param request
     * @return
     */
    @GetMapping("/infos/user")
    public Result queryByUid(@RequestParam(defaultValue = "0") String page,
                             @RequestParam(defaultValue = "5") String pagesize,
                             HttpServletRequest request) {
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if (uid == 0) {
            return Result.fail(MagicVariable.BAD_REQUEST);
        }
        Map<String, String> req_map = new HashMap<>();
        req_map.put("page", page);
        req_map.put("pagesize", pagesize);
        int int_page = Integer.parseInt(page);
        int int_pagesize = Integer.parseInt(pagesize);
        PageHelper.startPage(int_page, int_pagesize);
        PageInfo<Info> infos = new PageInfo<>(this.infoService.queryByUid(uid));
        return Result.success(infos, req_map);
    }

    /*管理员业务*/

    /**
     * 查询所有的停车场信息
     *
     * @param page
     * @param pagesize
     * @param request
     * @return
     */
    @GetMapping("/admin/infos")
    public Result list(@RequestParam(defaultValue = "0") String page,
                       @RequestParam(defaultValue = "5") String pagesize,
                       HttpServletRequest request) {
        boolean isAdmin;
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
            isAdmin = MyTool.isAdmin(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if (isAdmin) {
            Map<String, String> req_map = new HashMap<>();
            req_map.put("page", page);
            req_map.put("pagesize", pagesize);
            int int_page = Integer.parseInt(page);
            int int_pagesize = Integer.parseInt(pagesize);
            PageHelper.startPage(int_page, int_pagesize);
            PageInfo<Info> infos = new PageInfo<>(this.infoService.queryAll());
            return Result.success(infos, req_map);
        } else {
            return Result.fail(MagicVariable.NOT_ADMIN);
        }
    }

    /**
     * 根据状态查询（未验证/验证通过/验证不通过）的停车位
     *
     * @param state    state = verified（验证通过） / no_verified（未验证） / disable（验证不通过：即不可用）
     * @param page
     * @param pagesize
     * @param request
     * @return
     */
    @GetMapping("/admin/infos/{state}")
    public Result listNoVerified(@PathVariable String state,
                                 @RequestParam(defaultValue = "0") String page,
                                 @RequestParam(defaultValue = "5") String pagesize,
                                 HttpServletRequest request) {
        boolean isAdmin;
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
            isAdmin = MyTool.isAdmin(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if (isAdmin) {
            if (!state.equals("verified") && !state.equals("disable") && !state.equals("no_verified"))
                return Result.fail(MagicVariable.INFO_STATE_IS_ERROR);
            Map<String, String> req_map = new HashMap<>();
            req_map.put("page", page);
            req_map.put("pagesize", pagesize);
            int int_page = Integer.parseInt(page);
            int int_pagesize = Integer.parseInt(pagesize);
            PageHelper.startPage(int_page, int_pagesize);
            PageInfo<Info> infos = new PageInfo<>(this.infoService.queryByState(state));
            return Result.success(infos, req_map);
        } else {
            return Result.fail(MagicVariable.NOT_ADMIN);
        }
    }


    /**
     * 管理员验证停车场，即将状态state修改为verified、disable
     *
     * @param pid
     * @param state
     * @param request
     * @return
     */
    @PutMapping("/admin/infos/{pid}")
    public Result verified(@PathVariable String pid,
                           @RequestParam String state, HttpServletRequest request) {
        boolean isAdmin;
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
            isAdmin = MyTool.isAdmin(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if (isAdmin) {
            if (pid == null)
                return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
            if (!"verified".equals(state) && !state.equals("disable") && !state.equals("no_verified"))
                return Result.fail(MagicVariable.INFO_STATE_IS_ERROR);
            int int_pid = Integer.parseInt(pid);
            Info info = this.infoService.queryById(int_pid);
            if (info == null)
                return Result.fail(MagicVariable.INFO_NOT_EXIST);
            info.setState(state);
            Date curr = new Date();
            info.setStateupdatedate(curr);
            int update = this.infoService.update(info);
            System.out.println(update);
            if (update == 1) {
                if (state.equals("verified")) {
                    TempUserDTO tempUserDTO = new TempUserDTO();
                    tempUserDTO.setToUid(info.getUid());
                    tempUserDTO.setReason("停车位通过验证");
                    tempUserDTO.setScore(50);
                    Result add_score_result = this.userClientService.addScore(request.getHeader(JWTConfig.HEADER_TOKEN_KEY), tempUserDTO);
                    if ("success".equals(add_score_result.getStatus()))
                        System.out.println("--LOG: 添加用户积分成功 --");
                    else
                        System.out.println("-- LOG: 添加用户积分失败 --");
                    return Result.success("验证通过");
                } else if (state.equals("disable")) {
                    return Result.success("已禁用");
                } else {
                    return Result.success("已设置为未验证状态");
                }
            } else {
                return Result.fail("验证失败");
            }
        } else {
            return Result.fail(MagicVariable.NOT_ADMIN);
        }
    }

    /*管理员删除通过ID删除停车位*/
   /* @DeleteMapping("/admin/infos/{pid}")
    public Result deleteInfos(@PathVariable Integer pid,HttpServletRequest request){
        boolean isAdmin;
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
            isAdmin = MyTool.isAdmin(token);
        }
        catch (NullPointerException e){
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if(isAdmin){
            if (pid == null)
                return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
            if(infoService.queryById(pid) == null)
                return Result.fail(MagicVariable.INFO_NOT_EXIST);
            int delete = infoService.deleteById(pid);
            if(delete == 1){

            }
            return null;
        }
        else{
            return Result.fail(MagicVariable.NOT_ADMIN);
        }
    }*/
}