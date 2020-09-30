package com.jdcloud.sdk.netty.getstart.zerocoppy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class FileClientHandler extends ChannelInboundHandlerAdapter {
//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
////        ByteBuf addr = ctx.alloc().buffer();
////        addr.writeBytes("C:/sdk".getBytes());
////        System.out.println("发送了");
////        ctx.write(addr, promise);
////        ctx.flush();
//
//        ctx.writeAndFlush("C://sdk");
//        System.out.println("发送了");
//    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush("C://sdk");
        System.out.println("发送了");

        ctx.close();
    }


}
