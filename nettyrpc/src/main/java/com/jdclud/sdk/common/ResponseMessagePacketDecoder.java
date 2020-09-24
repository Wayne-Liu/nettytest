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
public class ResponseMessagePacketDecoder extends ByteToMessageDecoder {

    private static Charset CHARSET = Charset.forName("UTF-8");
    private final Serializer serializer;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ResponseMessagePacket packet = new ResponseMessagePacket();

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
        //error code
        packet.setErrCode(in.readLong());
        // message
        int messageLength = in.readInt();
        packet.setMessage(in.readCharSequence(messageLength,CHARSET).toString());
        // payload
        int payLoadLength = in.readInt();
        packet.setPayload(in.readBytes(payLoadLength));

        out.add(packet);

    }
}
