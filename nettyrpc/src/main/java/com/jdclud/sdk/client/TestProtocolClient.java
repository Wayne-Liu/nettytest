package com.jdclud.sdk.client;

import com.alibaba.fastjson.JSON;
import com.jdclud.sdk.common.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestProtocolClient {

    public static void main(String[] args) {
        int port = 9092;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new RequestMessagePacketEncoder(FastJsonSerializer.X));
                    ch.pipeline().addLast(new ResponseMessagePacketDecoder(FastJsonSerializer.X));
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<ResponseMessagePacket>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ResponseMessagePacket packet) throws Exception {

                            Object payloadObj = packet.getPayload();
                            if (payloadObj instanceof ByteBuf) {
                                ByteBuf byteBuf = (ByteBuf)payloadObj;
                                int byteBufLength = byteBuf.readableBytes();
                                byte[] bytes = new byte[byteBufLength];
                                byteBuf.readBytes(bytes);
                                payloadObj = FastJsonSerializer.X.decode(bytes, String.class);
                                packet.setPayload(payloadObj);
                            }

                            log.info("接收到来自服务端的响应消息内容为{}", JSON.toJSONString(packet));

                            //ctx.close();
                        }
                    });
                }
            });

            ChannelFuture future = bootstrap.connect("localhost",port).sync();
            log.info("启动NettyClient[{}]成功...",port);
            Channel channel = future.channel();
            RequestMessagePacket packet = new RequestMessagePacket();
            packet.setMagicNumber(3);
            packet.setVersion(4);
            packet.setSerialNumber("generateSerialNumber()");
            packet.setMessageType(MessageType.REQUEST);
            packet.setInterfaceName("club.throwable.contract.HelloService");
            packet.setMethodName("sayHello");
            packet.setMethodArgumentSignatures(new String[]{"java.lang.String"});
            packet.setMethodArguments(new Object[]{"doge"});
            channel.writeAndFlush(packet);
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}
