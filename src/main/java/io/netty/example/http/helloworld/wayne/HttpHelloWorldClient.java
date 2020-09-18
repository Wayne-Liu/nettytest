package io.netty.example.http.helloworld.wayne;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpHelloWorldClient {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 8080;
        EventLoopGroup workLoop = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workLoop);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new HttpClientCodec())
                            .addLast(new ClientRes1Adapter())
                            .addLast(new ClientRes2Adapter())
                            .addLast(new HttpClientRespHandler())

                    ;
                }
            });

            ChannelFuture f = b.connect(host, port).sync();


            //发送请求
            URI uri = new URI("http://127.0.0.1:8080");
            String msg = "hello";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

            request.headers().add(HttpHeaderNames.HOST, host);
            request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);


            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } catch (InterruptedException | URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            workLoop.shutdownGracefully();
        }


    }

}
