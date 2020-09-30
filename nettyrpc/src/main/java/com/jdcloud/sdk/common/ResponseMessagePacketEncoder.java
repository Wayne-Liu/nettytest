package com.jdcloud.sdk.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

import java.nio.charset.Charset;
import java.util.Map;

//http://throwable.club/2020/01/12/netty-custom-rpc-framework-protocol/
@RequiredArgsConstructor
public class ResponseMessagePacketEncoder extends MessageToByteEncoder<ResponseMessagePacket> {

    private final Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessagePacket packet, ByteBuf out) throws Exception {
        // 魔数
        out.writeInt(packet.getMagicNumber());
        //版本
        out.writeInt(packet.getVersion());
        //流水号
        out.writeInt(packet.getSerialNumber().length());
        out.writeCharSequence(packet.getSerialNumber(), Charset.forName("UTF-8"));
        //消息类型
        out.writeByte(packet.getMessageType().getType());
        // 附件
        Map<String,String> attachments = packet.getAttachments();
        out.writeInt(attachments.size());
        attachments.forEach((k,v) ->{
            out.writeInt(k.length());
            out.writeCharSequence(k, Charset.forName("UTF-8"));
            out.writeInt(v.length());
            out.writeCharSequence(v, Charset.forName("UTF-8"));
        });
        // errCode
        out.writeLong(packet.getErrCode());
        //message
        out.writeInt(packet.getMessage().length());
        out.writeCharSequence(packet.getMessage(), Charset.forName("UTF-8"));
        //payload
        byte[] serials = serializer.encode(packet.getPayload());
        out.writeInt(serials.length);
        out.writeBytes(serials);

    }
}
