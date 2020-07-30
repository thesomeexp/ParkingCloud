package cn.edu.lingnan.controller;

import cn.edu.lingnan.dao.ReviewDao;
import cn.edu.lingnan.entity.Info;
import cn.edu.lingnan.entity.Review;
import cn.edu.lingnan.service.InfoClientService;
import cn.edu.lingnan.service.ReviewService;
import cn.edu.lingnan.util.JWTConfig;
import cn.edu.lingnan.util.MagicVariable;
import cn.edu.lingnan.util.MyTool;
import cn.edu.lingnan.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Review)表控制层
 *
 * @author makejava
 * @since 2020-06-04 11:22:24
 */
@RestController
public class ReviewController {
    /**
     * 服务对象
     */
    @Resource
    private ReviewService reviewService;

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private InfoClientService infoClientService;

    /*
     * 插入一条评论
     * */
    @PostMapping("/review/{pid}")
    public Result addReview(@PathVariable String pid, @RequestParam String accuracy,
                            @RequestParam String easytofind, @RequestParam String content,
                            HttpServletRequest request) throws Exception {
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
        if(MyTool.isStringEmpty(pid, accuracy, easytofind))
            return Result.fail(MagicVariable.BAD_REQUEST);
        if(content == null || MyTool.isStringEmpty(content))
            content = MagicVariable.DEFAULT_REVIEW;
        int int_pid = Integer.parseInt(pid);
        int int_accuracy = Integer.parseInt(accuracy);
        int int_easyToFind = Integer.parseInt(easytofind);
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

        if (!MyTool.isStarLegal(int_accuracy, int_easyToFind))
            return Result.fail(MagicVariable.STAR_ILLEGAL);
        Review review = new Review();
        review.setUid(uid);
        review.setPid(int_pid);
        Date curr = new Date();
        review.setSubmitdate(curr);
        review.setAccuracy(int_accuracy);
        review.setEasytofind(int_easyToFind);
        review.setContent(HtmlUtils.htmlEscape(content));
        reviewService.insert(review);
        return Result.success(MagicVariable.REVIEW_ADD_SUCCESS);
    }
    /*
    * 查询某个停车场的评论信息
    * */
    @GetMapping("/review/{pid}")
    public Result listByPid(@PathVariable Integer pid,
                          @RequestParam(defaultValue = "0") String page,
                          @RequestParam(defaultValue = "5") String pagesize){
        if(pid == null)
            return Result.fail(MagicVariable.INFO_ID_IS_EMPTY);
        Map<String,String> req_map = new HashMap<>();
        req_map.put("page",page);
        req_map.put("pagesize",pagesize);
        int int_page =  Integer.parseInt(page);
        int int_pagesize = Integer.parseInt(pagesize);
        PageHelper.startPage(int_page,int_pagesize);
        PageInfo<Review> reviews =  new PageInfo<>(this.reviewService.queryByPid(pid));
        return Result.success(reviews,req_map);
    }

    /*
    * 查询用户自己提交的评论信息
    * */
    @GetMapping("/review")
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
        if(uid == 0)
            return Result.fail(MagicVariable.BAD_REQUEST);
        Map<String,String> req_map = new HashMap<>();
        req_map.put("page",page);
        req_map.put("pagesize",pagesize);
        int int_page =  Integer.parseInt(page);
        int int_pagesize = Integer.parseInt(pagesize);
        PageHelper.startPage(int_page,int_pagesize);
        List<Review> rl = this.reviewService.queryByUid(uid);
        PageInfo<Review> prl = new PageInfo<>(rl);
        return  Result.success(prl,req_map);
    }
    /*管理员业务*/
    /*
    *管理员通过ID删除某条评论
    * */
    @DeleteMapping("/admin/review/{id}")
    public Result delById(@PathVariable Integer id,
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
            if(id == null)
                return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
            if(this.reviewService.queryById(id) == null)
                return Result.fail("不存在该ID的评论信息");
            int i = this.reviewService.deleteById(id);
            if(i == 1)
                return Result.success("评论删除成功");
            else
                return Result.fail("评论删除失败");
        }
        else{
            return Result.fail(MagicVariable.NOT_ADMIN);
        }
    }
    /*
    * 管理员：通过ID批量删除评论信息
    * */
    @DeleteMapping("/admin/review")
    public Result delMore(@RequestParam int[] ids,HttpServletRequest request){
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
            if(ids == null)
                return Result.fail("未选择要删除的记录");
            for(int id : ids){
                if(this.reviewService.queryById(id) == null)
                    return Result.fail("不存在ID为"+id+"的评论信息");
                this.reviewService.deleteById(id);
            }
            return Result.success("删除成功");
        }
        else{
            return Result.fail(MagicVariable.NOT_ADMIN);
        }
    }
}