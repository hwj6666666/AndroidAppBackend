package org.example.jiaoji.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyWebSocketStarter {

    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketStarter.class);
    private static EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Autowired
    private static HandlerWebSocket handlerWebSocket;

    public static void run() {
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //对http协议的支持，使用http的解码器
                            pipeline.addLast(new HttpServerCodec());
                            //聚合解码，httpRequest/httpContent,保证接受http请求的完整性
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            //客户端心跳
                            pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(handlerWebSocket);
                            //将http协议升级为ws协议，支持websocket
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536, true, true, 10000L));
                            pipeline.addLast(new HandlerWebSocket());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(1896).sync();
            logger.info("netty启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            logger.error("启动netty失败");
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void close(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
