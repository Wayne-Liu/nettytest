package com.jdcloud.sdk.client;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jdcloud.sdk.common.MessageType;
import com.jdcloud.sdk.common.RequestMessagePacket;
import io.netty.channel.Channel;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentMap;

public class ContractProxyFactory {

    private static final RequestArgumentExtractor EXTRACTOR = new DefaultRequestExtactor();
    private static final ConcurrentMap<Class<?>, Object> CACHE = Maps.newConcurrentMap();

    public static <T> T ofProxy(Class<T> interfaceKlass) {
        return (T)CACHE.computeIfAbsent(interfaceKlass, x ->
                Proxy.newProxyInstance(interfaceKlass.getClassLoader(), new Class[]{interfaceKlass},(target,method,args) -> {
                    RequestArgumentExtractInput input = new RequestArgumentExtractInput();
                    input.setInterfaceKlass(interfaceKlass);
                    input.setMethod(method);
                    RequestArgumentExtractOutput output = EXTRACTOR.extract(input);
                    //封装请求参数
                    RequestMessagePacket packet =new RequestMessagePacket();
                    packet.setMagicNumber(3);
                    packet.setVersion(4);
                    packet.setSerialNumber("5");
                    packet.setMessageType(MessageType.REQUEST);
                    packet.setInterfaceName(output.getInterfaceName());
                    packet.setMethodName(output.getMethodName());
                    packet.setMethodArgumentSignatures(output.getMethodArgumentSignatures().toArray(new String[0]));
                    packet.setMethodArguments(args);
                    Channel channel = ClientChannelHolder.channel_reference.get();
                    channel.writeAndFlush(packet);

                    return String.format("[%s#%s]调用成功,发送了[%s]到NettyServer[%s]", output.getInterfaceName(),
                            output.getMethodName(), JSON.toJSONString(packet), channel.remoteAddress());

                }));
    }


}
