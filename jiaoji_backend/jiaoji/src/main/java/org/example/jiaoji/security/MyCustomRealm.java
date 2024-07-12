package org.example.jiaoji.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.jwt.JWTUtil;

import java.nio.charset.StandardCharsets;

@Component
public class MyCustomRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权，启动！");
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //我们的项目中，所有用户的权限都是一致的，所以只象征性的设置user类型与read权限
        authorizationInfo.addRole("user");
        authorizationInfo.addStringPermission("read");

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("token验证启动");
        String email = token.getPrincipal().toString();
        String password = token.getCredentials().toString();

        RetType res=userService.Login(email, password);
        if (res.isOk())
            return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
        else throw new IncorrectCredentialsException("密码错误");
    }
}
