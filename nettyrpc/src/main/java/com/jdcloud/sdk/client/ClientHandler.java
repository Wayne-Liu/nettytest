package com.jdcloud.sdk.client;

import com.alibaba.fastjson.JSON;
import com.jdcloud.sdk.client.sync.ResponseFuture;
import com.jdcloud.sdk.common.ResponseMessagePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<ResponseMessagePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessagePacket packet) throws Exception {
        log.info("接收到响应包,内容:{}", JSON.toJSONString(packet));
        ResponseFuture responseFuture = ContractProxyFactory.RESPONSE_FUTURE_TABLE.get(packet.getSerialNumber());
        if (null != responseFuture) {
            responseFuture.putResponse(packet);
        } else {
            log.warn("接收响应包查询ResponseFuture不存在,请求ID:{}", packet.getSerialNumber());
        }

    }
}
