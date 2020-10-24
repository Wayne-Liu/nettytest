package com.jdcloud.sdk.client;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicReference;

public class ClientChannelHolder {

    public static final AtomicReference<Channel> channel_reference = new AtomicReference<>();
}
