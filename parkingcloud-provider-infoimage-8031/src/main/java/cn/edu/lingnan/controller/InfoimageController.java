package cn.edu.lingnan.controller;

import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.entity.Infoimage;
import cn.edu.lingnan.service.InfoClientService;
import cn.edu.lingnan.service.InfoimageService;
import cn.edu.lingnan.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Infoimage)表控制层
 *
 * @author makejava
 * @since 2020-06-04 11:28:15
 */
@RestController
public class InfoimageController {

    @Resource
    private InfoimageService infoimageService;

    @Autowired
    private InfoClientService infoClientService;

    /**
     * 添加停车场的详情图片
     * 校验: pid是否存在, image是否为空, 停车场是否存在
     * 插入数据, 存储图片
     * @param pid 停车位ID
     * @param image 该停车位的图片
     * @param request   登录信息请求
     * @return
     * @throws Exception
     */
    @PostMapping("/infoimage")
    public Result insert(String pid, MultipartFile image, HttpServletRequest request) throws Exception{
        int id;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            id = MyTool.getUserId(token);
        }
        catch (NullPointerException e){
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if (MyTool.isStringEmpty(pid))
            return Result.fail(MagicVariable.BAD_REQUEST);
        if (image == null || image.isEmpty())
            return Result.fail(MagicVariable.INFO_IMAGE_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        //判断是否存在该车位
        Result get_info_result = infoClientService.get(int_pid);
        if (get_info_result.getStatus().equals(Result.FAIL_CODE)) {
            System.out.println("返回失败了000");
            return Result.fail(get_info_result.getMessage());
        }
        System.out.println("停车场存在");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> get_map = (Map<String, Object>) get_info_result.getData();
        Info info = mapper.convertValue(get_map.get("info"), Info.class);

        System.out.println(info);
        if(info == null)
            return Result.fail(MagicVariable.INFO_NOT_EXIST);

        Infoimage newInfoImage = new Infoimage(); 
        newInfoImage.setUid(id);
        newInfoImage.setPid(int_pid);
        newInfoImage.setSubmitdate(new Date());
        newInfoImage.setState(MagicVariable.NO_VERIFIED);
        infoimageService.insert(newInfoImage);
        Result result = saveOrUpdateImageFile(newInfoImage, image, request);// 添加多张详情图片
        if(result.getStatus().equals("success")) {
            return Result.success("停车位图片添加成功");
        }
        else{
            this.infoimageService.deleteById(newInfoImage.getId());
            return result;
        }
    }
    // 添加图片bug: 如果不是png, jpg文件就会抛出异常, 后期可以做一下图片压缩
    public Result saveOrUpdateImageFile(Infoimage bean, MultipartFile image, HttpServletRequest req) throws Exception {
        InputStream inputStream;
        boolean result = false;
        try {
            inputStream = image.getInputStream();
            result = FtpUtil.uploadImage(inputStream, bean.getId() + ".jpg", "/home/nginx/www/image/info_detail/"+bean.getPid()+"/");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("停车位图片上传的io异常,停车位图片添加失败");
        }
        if (result) {
            return Result.success(MagicVariable.INFO_ADD_SUCCESS);
        } else {
            return Result.fail("停车位图片添加失败");
        }
    }
    /*
    * 查询某个停车场的详细信息
    * */
    @GetMapping("/infoimage")
    public Result listImage(String pid,HttpServletRequest request) throws Exception {
        if (MyTool.isStringEmpty(pid))
            return Result.fail(MagicVariable.INFO_ID_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        //判断是否存在该车位
        Result get_info_result = infoClientService.get(int_pid);
        if (get_info_result.getStatus().equals(Result.FAIL_CODE)) {
            System.out.println("返回失败了000");
            return Result.fail(get_info_result.getMessage());
        }
        System.out.println("停车场存在");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> get_map = (Map<String, Object>) get_info_result.getData();
        Info info = mapper.convertValue(get_map.get("info"), Info.class);

        System.out.println(info);
        if(info == null)
            return Result.fail(MagicVariable.INFO_NOT_EXIST);

        List<Infoimage> infoimages = infoimageService.queryByPid(int_pid);
        Map<String,String> req_map = new HashMap<>();
        req_map.put("pid",pid);
        Map<String,Object> map = new HashMap<>();
        map.put("infoimages",infoimages);
        return Result.success(map,req_map);
    }
    /*
    *查询用户提交的停车场详情图片
    * */
    @GetMapping("/infoimage/user")
    public Result listByUid(@RequestParam(defaultValue = "0") String page,
                            @RequestParam(defaultValue = "5") String pagesize,
                            HttpServletRequest request){
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
        }
        catch (NullPointerException e){
            return Result.fail(MagicVariable.UN_LOGIN);
        }
        if(uid == 0) {
            return Result.fail(MagicVariable.BAD_REQUEST);
        }
        Map<String,String> req_map = new HashMap<>();
        req_map.put("page",page);
        req_map.put("pagesize",pagesize);
        int int_page =  Integer.parseInt(page);
        int int_pagesize = Integer.parseInt(pagesize);
        PageHelper.startPage(int_page,int_pagesize);
        PageInfo<Infoimage> infoimages = new PageInfo<>(this.infoimageService.queryByUid(uid));
        return Result.success(infoimages,req_map);
    }

    /*管理员的业务*/
    /*
    * 查询 未验证/已验证/已禁用 用户提交的停车场详情图片
    * */
    @GetMapping("/admin/infoimage/{state}")
    public Result listByState(@PathVariable String state,
                              @RequestParam(defaultValue = "0") String page,
                              @RequestParam(defaultValue = "5") String pagesize,
                              HttpServletRequest request){
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
        if(isAdmin) {
            if(!state.equals("verified") && !state.equals("disable") && !state.equals("no_verified"))
                return Result.fail(MagicVariable.INFO_STATE_IS_ERROR);
            Map<String,String> req_map = new HashMap<>();
            req_map.put("page",page);
            req_map.put("pagesize",pagesize);
            int int_page =  Integer.parseInt(page);
            int int_pagesize = Integer.parseInt(pagesize);
            PageHelper.startPage(int_page,int_pagesize);
            PageInfo<Infoimage> infoimages = new PageInfo<>(this.infoimageService.queryByState(state));
            return Result.success(infoimages,req_map);
        }
        else{
            return Result.fail(MagicVariable.NOT_ADMIN);
        }
    }
    /*
    * 管理员 验证通过/禁用/可用 用户提交的停车场详情图片
    * */
    @PutMapping("/admin/infoimage/{id}")
    public Result updateStateById(@PathVariable Integer id,
                                  @RequestParam String state,
                                  HttpServletRequest request){
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
            if(id == null)
                return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
            if(!state.equals("verified") && !state.equals("disable") && !state.equals("no_verified"))
                return Result.fail(MagicVariable.INFO_STATE_IS_ERROR);
            Infoimage infoimage = this.infoimageService.queryById(id);
            if(infoimage == null)
                return Result.fail("不存在该ID的图片提交信息");
            infoimage.setState(state);
            Date curr = new Date();
            infoimage.setSubmitdate(curr);
            int update = this.infoimageService.updateStateById(infoimage);
            if(update == 1){
                if(state.equals("verified")){
                    return Result.success("验证通过");
                }else if(state.equals("disable")){
                    return Result.success("已禁用");
                }else{
                    return Result.success("已设置为未验证状态");
                }
            }
            else {
                return Result.fail("验证失败");
            }
        }
        return null;
    }
}