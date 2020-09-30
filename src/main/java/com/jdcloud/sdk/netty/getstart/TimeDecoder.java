package com.jdcloud.sdk.netty.getstart;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        out.add(new UnixTime(byteBuf.readUnsignedInt()));
    }
}
