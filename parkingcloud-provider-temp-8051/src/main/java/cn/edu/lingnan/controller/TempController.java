package cn.edu.lingnan.controller;

import cn.edu.lingnan.dto.TempUserDTO;
import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.entity.Temp;
import cn.edu.lingnan.entity.Tempuser;
import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.service.InfoClientService;
import cn.edu.lingnan.service.TempService;
import cn.edu.lingnan.service.TempuserService;
import cn.edu.lingnan.service.UserClientService;
import cn.edu.lingnan.util.JWTConfig;
import cn.edu.lingnan.util.MagicVariable;
import cn.edu.lingnan.util.MyTool;
import cn.edu.lingnan.util.Result;
import cn.edu.lingnan.vo.TempUserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * (Temp)表控制层
 *
 * @author makejava
 * @since 2020-06-04 11:16:28
 */
@RestController
public class TempController {
    /**
     * 服务对象
     */
    @Resource
    private TempService tempService;

    @Autowired
    private InfoClientService infoClientService;

    @Autowired
    private TempuserService tempuserService;

    @Autowired
    private UserClientService userClientService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
//    @GetMapping("selectOne")
//    public Temp selectOne(Integer id) {
//        return this.tempService.queryById(id);
//    }

    /**
     * 添加停车场的状态
     * 1. 插入数据库
     * 2. 调用info的方法修改info
     * 3. 判断是否修改成功, 如果失败则删除此条temp
     *
     * @param pid
     * @param location
     * @param state
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/temp/{pid}")
    public Result addTemp(@PathVariable("pid") int pid, String location, String state, HttpServletRequest request) throws Exception {
        String token;
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        } catch (Exception e) {
            return Result.fail(JWTConfig.JWP_EXCEPTION);
        }
        // 判断参数是否存在
        if (MyTool.isStringEmpty(location))
            return Result.fail(MagicVariable.LOCATION_IS_EMPTY);
        if (MyTool.isStringEmpty(state))
            return Result.fail(MagicVariable.STAR_IS_EMPTY);
        // 解析参数, 判断是否合法
        double[] xyArray = MyTool.praseLocation(location);
        double x = xyArray[0];
        double y = xyArray[1];
        int int_state = Integer.parseInt(state);
        if (!MyTool.isStateLegal(int_state))
            return Result.fail(MagicVariable.INFO_STATE_IS_ERROR);
        if (!MyTool.isXYLegal(x, y))
            return Result.fail(MagicVariable.INFO_LOCATION_ILLEGAL);
        // TODO: 判断插入距离
        // 判断用户距离上次插入时间是否在30分钟内 有的话返回失败
        long now = System.currentTimeMillis();
        long interval_time = MagicVariable.INTERVAL_TIME;
        Date beforeDate = new Date(now - interval_time);
        List<Temp> old_temp = tempService.getByPidAndUidAndSubmitDateAfterThan(uid, pid, beforeDate);
        System.out.println("最近30分钟插入的temp:" + (old_temp.isEmpty() == true ? "无" : "有"));
        for (Temp t : old_temp) {
            System.out.println(t);
        }
        if (!old_temp.isEmpty())
            return Result.fail(MagicVariable.INTERVAL_TIME_ERROR);
        // 判断停车场是否存在
        System.out.println("pid" + pid);
        Result get_info_result = infoClientService.get(pid);
        if (get_info_result.getStatus().equals(Result.FAIL_CODE)) {
            System.out.println("返回失败了000");
            return Result.fail(get_info_result.getMessage());
        }
        System.out.println("停车场存在");

        // feign LinkedHashMap incompatible with
        /*
        因为rpc远程调用在底层还是使用的HTTPClient，所以在传递参数的时候，必定要有个顺序，当你传递map的时候map里面的值也要有顺序，
        不然服务层在接的时候就出问题了，所以它才会从map转为linkedhashMap！spring 有一个类叫ModelMap，继承了
        linkedhashMap public class ModelMap extends LinkedHashMap ,所以一个接口返回的结果就可以直接用ModelMap来接，
        注意ModelMap是没有泛型的，不管你返回的结果是什么类型的map，泛型是多复杂的map，都可以直接new一个Modelmap，用它来接返回的结果！！！
        出处: https://blog.csdn.net/hp_yangpeng/article/details/80592332
         */
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> get_map = (Map<String, Object>) get_info_result.getData();
        Info info = mapper.convertValue(get_map.get("info"), Info.class);
//        User u = mapper.convertValue(get_map.get("user"), User.class);
        if (info == null)
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        // 插入temp
        int t = LocalDateTime.now().getHour() - 1;
        // TODO: 抛出异常没有处理
        if (t == -1)
            t = 23;
        String tField = "t" + t;
        System.out.println("得到当前" + tField);
        Field f = null;
        f = info.getClass().getDeclaredField(tField);
        f.setAccessible(true);
        int old_state = Integer.parseInt(f.get(info).toString());
        System.out.println("更新前状态值: " + old_state);
        int target_state = (old_state + int_state) / 2;
        System.out.println("更新目标值: " + target_state);
        Temp temp = new Temp();
        temp.setUid(uid);
        temp.setPid(pid);
        temp.setState(int_state);
        int insert_result = tempService.insert(temp);
        temp.setState(target_state);
        // 更新info数据, 先从now获取小时
//        SimpleDateFormat formatter = simpleDateFormat.format();
        System.out.println("插入返回结果: " + insert_result);
        System.out.println("传递前temp: " + temp);
        Result put_result_Temp = infoClientService.put(request.getHeader(JWTConfig.HEADER_TOKEN_KEY), temp);
        if (put_result_Temp.getStatus().equals("fail")) {
            System.out.println("失败了111删除Temp内容");
            tempService.deleteById(temp.getId());
            System.out.println(put_result_Temp.getMessage());
            return Result.fail(put_result_Temp.getMessage());
        }
        return Result.success();
    }

    /**
     * 根据tid返回最新的5条有价值数据
     *
     * @param pid
     * @return
     */
    @GetMapping("/tempDetail/{pid}")
    public Result getTu(@PathVariable("pid") int pid, HttpServletRequest request) {
        String token;
        int uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            System.out.println("header: " + header);
            token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            System.out.println(token);
            uid = MyTool.getUserId(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        } catch (Exception e) {
            return Result.fail(JWTConfig.JWP_EXCEPTION);
        }
        List<Temp> ltl = this.tempService.queryLastByLimitDesc(pid, 5);
        // 制作vo
        List<TempUserVO> tuvol = new ArrayList<>();
        if (!ltl.isEmpty()) {
            // FIXME: 性能问题, 这里会查询n次数据库, 目的是为了查询用户点了有用还是无用
            for (Temp t : ltl) {
                Integer opt = tempuserService.searchUserOptDESC(t.getId(), uid);
                if (opt == null)
                    opt = -1;
                TempUserVO tvo = new TempUserVO();
                tvo.setState(t.getState());
                tvo.setSubmitdate(t.getSubmitdate());
                tvo.setUseful(t.getUseful());
                tvo.setBad(t.getBad());
                tvo.setTid(t.getId());
                tvo.setUid(t.getUid());
                tvo.setPid(t.getPid());
                tvo.setMyOpt(opt);
                tuvol.add(tvo);
            }
        }
        Map<String, Object> res_map = new HashMap<>();
        res_map.put("tempvol", tuvol);
        return Result.success(res_map);
    }

    /**
     * @param req_tid temp记录id
     * @param req_opt 是否有价值, 1为有价值, 0为无价值
     * @param request
     * @return
     */
    @PostMapping("/useful")
    public Result putUseful(Integer req_tid, Integer req_opt, HttpServletRequest request) {
        String token;
        int req_uid;
        try {
            String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
            token = header.replace(JWTConfig.TOKEN_PREFIX, "");
            req_uid = MyTool.getUserId(token);
        } catch (NullPointerException e) {
            return Result.fail(MagicVariable.UN_LOGIN);
        } catch (Exception e) {
            return Result.fail(JWTConfig.JWP_EXCEPTION);
        }
        Temp temp = tempService.queryById(req_tid);
        if (temp == null)
            return Result.fail("状态不存在");

        /**
         * 1. 先查询用户之前的选择
         * 2. 根据状态更新 temp 信息
         * 3. 查询tempUser是否有记录, 有则更新无则插入
         * 4. 更新积分
         * FIXME: 这里可以优化一下代码(添加事务)
         */
        int score;
        Integer before_opt = tempuserService.searchUserOptDESC(req_tid, req_uid);
        if (req_opt == 1 && (before_opt == null || before_opt == -1)) {
            tempService.addUsefulCount(req_tid);
            score = 10;
        } else if (req_opt == 1 && before_opt == 0) {
            tempService.updateUsefulCount(req_tid);
            score = 20;
        }else if (req_opt == 0 && (before_opt == null || before_opt == -1)) {
            tempService.addBadCount(req_tid);
            score = -10;
        } else if (req_opt == 0 && before_opt == 1) {
            tempService.updateBadCount(req_tid);
            score = -20;
        } else {
            return Result.fail("无匹配的temp修改选项");
        }
        if (before_opt == null || before_opt == -1) {
            tempuserService.addSelect(req_uid, req_tid, req_opt);
        } else if (before_opt == 1) {
            tempuserService.updateSelect(req_uid, req_tid, req_opt);
        } else {
            return Result.fail("无匹配的opt选项");
        }
        if (req_uid == temp.getUid()) // 自己给自己投票时积分不更新
            score = 0;
        TempUserDTO tempUserDTO = new TempUserDTO();
        tempUserDTO.setToUid(temp.getUid());
        tempUserDTO.setReason("修改状态");
        tempUserDTO.setScore(score);
        Result add_score_result = this.userClientService.addScore(request.getHeader(JWTConfig.HEADER_TOKEN_KEY), tempUserDTO);
        if ("success".equals(add_score_result.getStatus()))
            System.out.println("--LOG: 添加用户积分成功--score: " + score);
        else
            System.out.println("-- LOG: 添加用户积分失败 --score: " + score);
        return Result.success();
    }

}