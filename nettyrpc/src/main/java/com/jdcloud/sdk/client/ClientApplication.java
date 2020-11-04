package com.jdcloud.sdk.client;

import com.alibaba.fastjson.JSON;
import com.jdcloud.sdk.common.*;
import com.jdcloud.sdk.server.contract.HelloService;
import com.jdcloud.sdk.server.contract.service.LookDir;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientApplication {

    public static void main(String[] args) throws InterruptedException {
        int port = 9092;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0,4,0,4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new RequestMessagePacketEncoder(FastJsonSerializer.X));
                            ch.pipeline().addLast(new ResponseMessagePacketDecoder(FastJsonSerializer.X));
//                            ch.pipeline().addLast(new SimpleChannelInboundHandler<ResponseMessagePacket>() {
//                                @Override
//                                protected void channelRead0(ChannelHandlerContext ctx, ResponseMessagePacket packet) throws Exception {
//                                    Object targetPayload = packet.getPayload();
//                                    if (targetPayload instanceof ByteBuf) {
//                                        ByteBuf byteBuf = (ByteBuf)targetPayload;
//                                        int readableByteLength = byteBuf.readableBytes();
//                                        byte[] bytes = new byte[readableByteLength];
//                                        byteBuf.readBytes(bytes);
//                                        targetPayload = FastJsonSerializer.X.decode(bytes, String.class);
//                                        byteBuf.release();
//                                    }
//                                    packet.setPayload(targetPayload);
//                                    log.info("接收到来自服务端的响应消息，消息内容：{}",JSON.toJSONString(packet));
//                                }
//                            });
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect("localhost",port).sync();

            ClientChannelHolder.channel_reference.set(future.channel());
            HelloService helloService = ContractProxyFactory.ofProxy(HelloService.class);
            String result = helloService.sayHello("wayne");
            log.info("helloService sayHello wayne result is:{}",result);

            LookDir dirService = ContractProxyFactory.ofProxy(LookDir.class);
            String dir = dirService.getDir("Dubbo");
            log.info("LookDir:{}",dir);

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

}
