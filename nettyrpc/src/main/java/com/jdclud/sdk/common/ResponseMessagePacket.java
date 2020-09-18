package com.jdclud.sdk.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseMessagePacket extends BaseMessagePacket {

    private long errCode;

    private String message;

    private Object payload;
}
