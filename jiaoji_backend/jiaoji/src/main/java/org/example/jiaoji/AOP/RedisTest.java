package org.example.jiaoji.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class RedisTest {
    @Around("execution(* org.example.jiaoji.controller.TopicController.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable{
        //记录开始时间
        long begin=System.currentTimeMillis();
        //运行方法
        Object result=joinPoint.proceed();
        //记录结束时间
        long ed=System.currentTimeMillis();    
		log.info(joinPoint.getSignature()+"method takes:{}ms",ed-begin);
        return result;
    }
}
