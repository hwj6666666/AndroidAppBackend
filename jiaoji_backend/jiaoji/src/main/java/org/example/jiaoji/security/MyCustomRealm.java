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
import cn.hutool.jwt.JWTUtil;

import java.nio.charset.StandardCharsets;

@Component
public class MyCustomRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //我们的项目中，所有用户的权限都是一致的，所以只象征性的设置user类型与read权限
        authorizationInfo.addRole("user");
        authorizationInfo.addStringPermission("read");

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        if (!JWTUtil.verify(accessToken,"MyConstant.JWT_SIGN_KEY".getBytes(StandardCharsets.UTF_8)))
            throw new IncorrectCredentialsException("token失效，请重新登录");

        String email = (String) JWTUtil.parseToken(accessToken).getPayload("email");
        Integer userId = userMapper.selectIdByEmail(email);
        if (userId == null)
            throw new UnknownAccountException("用户不存在!");
        User user=userMapper.selectByUserId(userId);

        return new SimpleAuthenticationInfo(user, accessToken, this.getName());
    }
}
