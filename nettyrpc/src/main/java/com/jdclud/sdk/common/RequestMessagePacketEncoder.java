package com.jdclud.sdk.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

import java.nio.charset.Charset;
import java.util.Map;

//http://throwable.club/2020/01/12/netty-custom-rpc-framework-protocol/
@RequiredArgsConstructor
public class RequestMessagePacketEncoder extends MessageToByteEncoder<RequestMessagePacket> {

    private final Serializer serializer;

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
        // 接口名
        out.writeInt(packet.getInterfaceName().length());
        out.writeCharSequence(packet.getInterfaceName(), Charset.forName("UTF-8"));
        // 方法名
        out.writeInt(packet.getMethodName().length());
        out.writeCharSequence(packet.getMethodName(), Charset.forName("UTF-8"));
        // 方法参数签名
        if (packet.getMethodArgumentSignatures() != null) {
            out.writeInt(packet.getMethodArgumentSignatures().length);
            for(String signature : packet.getMethodArgumentSignatures()) {
                out.writeInt(signature.length());
                out.writeCharSequence(signature, Charset.forName("UTF-8"));
            }
        } else {
            out.writeInt(0);
        }
        // 方法参数
        if (packet.getMethodArguments() != null) {
            out.writeInt(packet.getMethodArguments().length);
            for(Object argument : packet.getMethodArguments()) {
                byte[] bytes = serializer.encode(argument);
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            }
        } else {
            out.writeInt(0);
        }
    }
}
