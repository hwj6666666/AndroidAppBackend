package org.example.jiaoji.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyCustomRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权启动");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("user");

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String email = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        Integer uid = userMapper.selectIdByEmail(email);
        if (uid == null)
            throw new UnknownAccountException("邮箱未注册");

        String encodedPassword = PasswordEncoder.encode(password, userMapper.selectSaltByUid(uid));
        uid = userMapper.selectIdByEmailAndPassword(email, encodedPassword);
        if (uid == null)
            throw new IncorrectCredentialsException("密码错误");

        User user = userMapper.selectByUserId(uid);
        if (user.getState() == 2)
            throw new LockedAccountException("用户已被封禁");

        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
    }
}
