package com.jdcloud.sdk.common;

import io.netty.buffer.ByteBuf;

public enum ByteBufferUtils {

    X;

    public byte[] readBytes(ByteBuf payload) {
        int payloadLength = payload.readableBytes();
        byte[] bytes = new byte[payloadLength];
        payload.readBytes(bytes);

        return bytes;
    }


}
