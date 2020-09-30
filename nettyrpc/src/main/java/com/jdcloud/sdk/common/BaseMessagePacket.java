package com.jdcloud.sdk.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class BaseMessagePacket implements Serializable {

    private int magicNumber;

    private int version;

    private String serialNumber;

    private MessageType messageType;

    private Map<String, String> attachments = new HashMap<>();

    public void addAttachment(String key, String value) {
        attachments.put(key, value);
    }
}
