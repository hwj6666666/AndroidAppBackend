package org.example.jiaoji.AOP;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.jiaoji.pojo.DataSourceType;
import org.example.jiaoji.utils.DataSourceContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class DataSourceAspect {

    @Before("execution(* org.example.jiaoji.mapper..*.select*(..)) ||execution(* org.example.jiaoji.mapper..*.search*(..)) || execution(* org.example.jiaoji.mapper..*.get*(..))")
    public void setReadDataSourceType() {
        DataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE);
        log.info("dataSource切换到：Read");
    }

    @Before("execution(* org.example.jiaoji.mapper..*.insert*(..)) ||execution(* org.example.jiaoji.mapper..*.sub*(..)) || execution(* org.example.jiaoji.mapper..*.add*(..)) || execution(* org.example.jiaoji.mapper..*.update*(..)) || execution(* org.example.jiaoji.mapper..*.delete*(..))")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
        log.info("dataSource切换到：Write");
    }
}