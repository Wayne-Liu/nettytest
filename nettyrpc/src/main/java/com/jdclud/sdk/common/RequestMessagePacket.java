package com.jdclud.sdk.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestMessagePacket extends BaseMessagePacket {

    private String interfaceName;

    private String methodName;

    private String[] methodArgumentSignatures;

    private Object[] methodArguments;
}
