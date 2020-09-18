package com.jdclud.sdk.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

import java.nio.charset.Charset;
//http://throwable.club/2020/01/12/netty-custom-rpc-framework-protocol/
@RequiredArgsConstructor
public class RequestMessagePacketEncoder extends MessageToByteEncoder<RequestMessagePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessagePacket packet, ByteBuf out) throws Exception {
        // 魔数
        out.writeInt(packet.getMagicNumber());
        //版本
        out.writeInt(packet.getVersion());
        //流水号
        out.writeInt(packet.getSerialNumber().length());
        out.writeCharSequence(packet.getSerialNumber(), Charset.forName("UTF-8"));
        //消息类型

        // 附件
        // 接口名
        // 方法名
        // 方法参数签名
        // 方法参数

    }
}
