package cn.edu.lingnan.realm;

import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.service.UserService;
import cn.edu.lingnan.util.MagicVariable;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JPARealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 给资源进行授权
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();

        // 添加资源的授权字符串
//        s.addStringPermission("user:add");

        // 获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();

        s.addStringPermission(user.getRole());
        if (user.getRole().equals(MagicVariable.ADMIN_ROLE))
            s.addStringPermission(MagicVariable.DEFAULT_ROLE);

//        s.addRole(user.getRole());
        return s;
    }

    /**
     * 执行认证逻辑
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userPhone = token.getPrincipal().toString();
        User que_user = new User();
        que_user.setPhone(userPhone);
        List<User> lu = userService.queryAll(que_user);
//        System.out.println("JPARealm: User: " + user.getName()+" " + user.getPhone()+" "+user.getPassword());
        if (lu.isEmpty()){
            // 手机号不存在
            return null; // 底层抛出UnKnowAccountException
        }
        User user = lu.get(0);
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, passwordInDB,
                ByteSource.Util.bytes(salt),
                getName());
        return authenticationInfo;
    }

}