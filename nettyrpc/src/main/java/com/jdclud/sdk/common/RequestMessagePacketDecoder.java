package com.jdclud.sdk.common;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class RequestMessagePacketDecoder extends ByteToMessageDecoder {

    private static Charset CHARSET = Charset.forName("UTF-8");
    private final Serializer serializer;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        RequestMessagePacket packet = new RequestMessagePacket();

        //魔数
        packet.setMagicNumber(in.readInt());
        //版本
        packet.setVersion(in.readInt());
        //流水号
        int serialNumberLength = in.readInt();
        packet.setSerialNumber(in.readCharSequence(serialNumberLength, CHARSET).toString());
        //消息类型
        packet.setMessageType(MessageType.fromValue(in.readByte()));
        //附件
        int attachmentsLength = in.readInt();
        Map<String, String> attachments = Maps.newHashMap();
        packet.setAttachments(attachments);
        if (attachmentsLength > 0) {
            for (int i=0;i<attachmentsLength;i++) {
                int keyLength = in.readInt();
                String key = in.readCharSequence(keyLength, CHARSET).toString();
                int valueLength = in.readInt();
                String value = in.readCharSequence(valueLength, CHARSET).toString();
                attachments.put(key, value);
            }
        }
        // 接口全类名
        int interfaceNameLength = in.readInt();
        packet.setInterfaceName(in.readCharSequence(interfaceNameLength, CHARSET).toString());
        // 方法名
        int methodNameLength = in.readInt();
        packet.setMethodName(in.readCharSequence(methodNameLength, CHARSET).toString());
        // 方法参数签名
        int signatureLength = in.readInt();
        if (signatureLength > 0) {
            String[] signatures = new String[signatureLength];
            for (int i=0;i<signatureLength;i++) {
                int keyLength = in.readInt();
                String signature = in.readCharSequence(keyLength, CHARSET).toString();
                signatures[i] = signature;
            }
            packet.setMethodArgumentSignatures(signatures);
        }
        // 方法参数
        int methodArgumentsLength = in.readInt();
        if (methodArgumentsLength > 0) {
            Object[] arguments = new Object[methodArgumentsLength];
            for (int i=0; i<methodArgumentsLength; i++) {
                int argumentLength = in.readInt();
                byte[] dst = new byte[argumentLength];
                in.readBytes(dst);
                Object argument = serializer.decode(dst, Object.class);
                arguments[i] = argument;
            }
            packet.setMethodArguments(arguments);
        }
        out.add(packet);

    }
}
