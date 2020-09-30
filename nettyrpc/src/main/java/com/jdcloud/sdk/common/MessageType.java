package com.jdcloud.sdk.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageType {

    REQUEST((byte)1),

    RESPONSE((byte)2),

    PING((byte)3),

    PONG((byte)4),

    NULL((byte)5),
    ;

    @Getter
    private final Byte type;

    public static MessageType fromValue(byte value) {
        for (MessageType type : MessageType.values()) {
            if (type.getType() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("value = %s", value));
    }
}
