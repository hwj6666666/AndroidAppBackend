package org.example.jiaoji.security;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;

@Configuration
public class ShiroConfig {

    @Bean
    public MyCustomRealm myCustomRealm() {
        return new MyCustomRealm();
    }

    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myCustomRealm());
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> customFilters = new LinkedHashMap<>();
        customFilters.put("jwt", new JWTFilter());
        shiroFilter.setFilters(customFilters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/user/login", "anon");
        filterMap.put("/mail/**", "anon");
        filterMap.put("/user/register", "anon");
        filterMap.put("/**", "auth");

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    //配置shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //负责shiro的生命周期
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
