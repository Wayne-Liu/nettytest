package com.jdclud.sdk.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.Charset;


@Slf4j
public class CuratorClient {

    public static void main(String[] args) throws Exception {
        String connectInfo = "47.95.140.142:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework client = CuratorFrameworkFactory
//                .newClient(connectInfo, 5000,3000,retryPolicy);

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectInfo)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("base")
                .build();

        client.start();

        //client.create().withMode(CreateMode.EPHEMERAL).forPath("/pathTemp","hello".getBytes());
        //删除节点
        //client.delete().forPath("/path");
        //client.create().forPath("/hello2","你个锤子".getBytes(Charset.forName("UTF-8")));

        //读取数据
        log.info("data:{}",new String(client.getData().forPath("/hello2"), Charset.forName("UTF-8")));

        //读取数据内容和stat


    }

}
