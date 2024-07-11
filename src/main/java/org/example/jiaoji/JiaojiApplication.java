package org.example.jiaoji;

import org.example.jiaoji.websocket.netty.NettyWebSocketStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories // repository 所在的包
@EntityScan
public class JiaojiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiaojiApplication.class, args);
        NettyWebSocketStarter.run();
    }
}