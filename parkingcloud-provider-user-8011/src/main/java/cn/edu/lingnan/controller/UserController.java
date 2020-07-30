package cn.edu.lingnan.controller;

import cn.edu.lingnan.dao.UserDao;
import cn.edu.lingnan.dto.TempUserDTO;
import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.service.UserService;
import cn.edu.lingnan.util.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-06-03 08:23:18
 */
@RestController
public class UserController {

    @Value("${eureka.instance.instance-id}")
    private String INSTANCE_ID;


    @Value("${FTP_HEAD_PATH}")
    private String FTP_HEAD_PATH;
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    public UserController() {
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
//    @GetMapping("selectOne")
//    public User selectOne(Integer id) {
//        System.out.println("1");
//        return this.userService.queryById(id);
//    }

    /**
     * 检查timestamp
     * 检查参数是否为空
     * 检查用户名, 手机号, 密码长度是否合法
     * 用户名手机号是否存在
     * 生成加密后的密码
     * 插入新用户
     * @param name
     * @param password
     * @param phone
     * @return
     */
    @PostMapping("/register")
    public Result register(String name, String password, String phone, HttpServletRequest request) {
        try {
            if (MyTool.isStringEmpty(name))
                return Result.fail(MagicVariable.USERNAME_IS_EMPTY);
            if (MyTool.isStringEmpty(password))
                return Result.fail(MagicVariable.PASSWORD_IS_EMPTY);
            if (MyTool.isStringEmpty(phone))
                return Result.fail(MagicVariable.PHONE_IS_EMPTY);

            if (!MyTool.isValueLengthLegal(name, MagicVariable.USER_NAME_MAX_LEN) ||
                    !MyTool.isValueLengthLegal(phone, MagicVariable.USER_PASSWORD_MAX_LEN) ||
                    !MyTool.isValueLengthLegal(password, MagicVariable.USER_PASSWORD_MAX_LEN))
                return Result.fail(MagicVariable.PARAM_VALUES_TOO_MAX);

            if (userService.isNameExist(name))
                return Result.fail(MagicVariable.USERNAME_IS_EXIST);
            if (userService.isPhoneExist(phone))
                return Result.fail(MagicVariable.PHONE_IS_EXIST);

            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = MagicVariable.HASH_TIMES;
            String algorithmName = MagicVariable.ALGORITHM_NAME;
            String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setSalt(salt);
            user.setPassword(encodedPassword);
            user.setRole(MagicVariable.DEFAULT_ROLE);
            user.setScore(0);
            userService.insert(user);
            return Result.success(MagicVariable.REGISTER_SUCCESS);
        } catch (Exception e) {
            return Result.fail(MagicVariable.LOGGER_ERROR_NO_LOGIN);
        }
    }

    @PostMapping("/login")
//    @CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST})
    public Result login(String phone, String password) {
        try {
            if (MyTool.isStringEmpty(phone))
                return Result.fail(MagicVariable.PHONE_IS_EMPTY);
            if (MyTool.isStringEmpty(password))
                return Result.fail(MagicVariable.PASSWORD_IS_EMPTY);
            /**
             * 使用shiro编写认证操作
             */
            // 1.获取Subject
            Subject subject = SecurityUtils.getSubject();
            // 2.封装用户数据
            UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
            // 3.执行登录方法
            subject.login(token);
            User user = (User) subject.getPrincipal();
            // 令牌签发时间
            Calendar issuedCal = Calendar.getInstance();
            Date date1 = issuedCal.getTime();
            // 令牌过期时间
            issuedCal.add(Calendar.HOUR_OF_DAY, JWTConfig.JWT_EXPIRE_HOUR);
            Date date2 = issuedCal.getTime();

            Algorithm algorithm = Algorithm.HMAC256(JWTConfig.JWT_SECRET); // 签名秘密
            // 每次登录签发JWT, 过期时间为 JWTConfig.JWT_EXPIRE_HOUR 小时
            String JWTToken = JWT.create()
                    .withIssuer(INSTANCE_ID)
                    .withIssuedAt(date1)
                    .withExpiresAt(date2)
                    .withClaim("id", user.getId())
                    .withClaim("name", user.getName())
                    .withClaim("isAdmin", user.getRole().equals(MagicVariable.ADMIN_ROLE) ? true : false)
                    .withClaim("score", user.getScore())
                    .sign(algorithm);

//            iss (issuer)：签发人
//            exp (expiration time)：过期时间
//            sub (subject)：主题
//            aud (audience)：受众
//            nbf (Not Before)：生效时间
//            iat (Issued At)：签发时间
//            jti (JWT ID)：编号

            Map<String, Object> res_map = new HashMap<>();
            res_map.put("token", JWTToken);
            return Result.success(res_map);
        } catch (UnknownAccountException e) {
            // 登录失败用户名不存在
            return Result.fail(MagicVariable.USER_PHONE_NOT_EXIST);
        } catch (IncorrectCredentialsException e) {
            // 密码错误
            return Result.fail(MagicVariable.USER_PASSWORD_ERROR);
        } catch (AuthenticationException e) {
            // 登录验证失败
            return Result.fail(MagicVariable.LOGIN_FAIL);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
            // 无效的token签名, 或者内容无法转换(应该不会触发吧?)
            return Result.fail(JWTConfig.JWP_EXCEPTION);
        } catch (Exception e) {
            e.printStackTrace();
            // 其他错误, 不登录就不做处理
            return Result.fail(MagicVariable.LOGGER_ERROR_NO_LOGIN);
        }

    }

//    使用示例
    @GetMapping("/get")
    public Result get(HttpServletRequest request
            , HttpServletResponse response) {
        String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
        if (header == null)
            return Result.fail(MagicVariable.UN_LOGIN);
        System.out.println("header: " + header);
        String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
        System.out.println(token);
        try {
            int id = MyTool.getUserId(token);
            String name = MyTool.getUserName(token);
            boolean isAdmin = MyTool.isAdmin(token);
            System.out.println("id:" + id);
            System.out.println("name: " + name);
            System.out.println("isAdmin: " + isAdmin);
        } catch (NullPointerException e) {
            System.out.println("空指针异常, 没有这个内容");
            e.printStackTrace();
            return Result.fail("Token异常, 没有相关数据");
        }
        return Result.success("敏感内容");
    }

    // 上传头像示例
    @PostMapping("/head")
//    @CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST})
    public Result addHead(MultipartFile image, HttpServletRequest request) throws Exception {
//        return Result.success("111");
         /*
        1、获取上传的文件流 inputStream, 获取用户id
        2、调用 FtpUtil 中的函数，将图片上传到图片服务器并返回成功/失败
         */
        if (image == null) {
            return Result.fail("image为空");
        }
        String header = request.getHeader(JWTConfig.HEADER_TOKEN_KEY);
        if (header == null)
            return Result.fail(MagicVariable.UN_LOGIN);
        System.out.println("header: " + header);
        String token = header.replace(JWTConfig.TOKEN_PREFIX, "");
        System.out.println(token);
        int id;
        try {
            id = MyTool.getUserId(token);
            System.out.println("id:" + id);
        } catch (NullPointerException e) {
            System.out.println("空指针异常, 没有这个内容");
            e.printStackTrace();
            return Result.fail("Token异常, 没有相关数据");
        }
        InputStream inputStream;
        boolean result = false;
        try {
            inputStream = image.getInputStream();
            result = FtpUtil.uploadImage(inputStream, id + ".jpg", FTP_HEAD_PATH);
        } catch (IOException e) {
            System.out.println("io异常");
            e.printStackTrace();
        }
//        String filename = image.getOriginalFilename();
        if (result) {
            return Result.success("上传头像成功");
        } else {
            return Result.fail("上传头像失败");
        }
    }

    @PutMapping("/score")
    public Result addScore(@RequestBody TempUserDTO tempuserDTO, HttpServletRequest request) {
        String token = MyTool.headerToToken(request.getHeader(JWTConfig.HEADER_TOKEN_KEY));
        boolean isValid = MyTool.isJWTAuthorized(token);
        if (!isValid)
            return Result.fail("JWT签名无效, 请重新登录");
        int fromUid = MyTool.getUserId(token);
        System.out.println("---LOG:" + fromUid + "影响" + tempuserDTO.getToUid() + "  " + tempuserDTO.getScore()
                + "积分---" + tempuserDTO.getReason());
        int result = this.userService.updateScore(tempuserDTO.getToUid(),  tempuserDTO.getScore());
        if (result == 1) {
            return Result.success();
        } else {
            return Result.fail("更改用户积分失败");
        }
    }




}