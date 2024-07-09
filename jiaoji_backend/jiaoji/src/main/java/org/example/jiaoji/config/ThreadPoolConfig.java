package org.example.jiaoji.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 核心线程数
        executor.setMaxPoolSize(20); // 最大线程数
        executor.setQueueCapacity(10000); // 队列容量
        executor.setKeepAliveSeconds(60); // 线程空闲时间
        executor.setThreadNamePrefix("MyExecutor-"); // 线程名称前缀
        executor.initialize(); // 初始化线程池
        return executor;
    }
}