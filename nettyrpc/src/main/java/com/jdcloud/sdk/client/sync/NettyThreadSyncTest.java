package com.jdcloud.sdk.client.sync;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
public class NettyThreadSyncTest {

    @ToString
    private static class ResponseFuture {
        private final long beginTimestamp = System.currentTimeMillis();
        @Getter
        private final long timeoutMilliseconds;
        @Getter
        private final String requestId;
        @Getter
        @Setter
        private volatile boolean sendRequestSucceed = false;
        @Setter
        @Getter
        private volatile Throwable cause;
        @Getter
        private volatile Object response;

        private final CountDownLatch latch = new CountDownLatch(1);

        public ResponseFuture( String requestId,long timeoutMilliseconds) {
            this.timeoutMilliseconds = timeoutMilliseconds;
            this.requestId = requestId;
        }

        public boolean timeout() {
            return System.currentTimeMillis() - beginTimestamp > timeoutMilliseconds;
        }

        public Object waitResponse(final long timeoutMilliseconds) throws InterruptedException {
            latch.await(timeoutMilliseconds, TimeUnit.MILLISECONDS);
            return response;
        }

        public void putResponse(Object response) {
            this.response = response;
            latch.countDown();
        }

    }

    static ExecutorService REQUEST_THREAD;
    static ExecutorService NETTY_IO_THREAD;
    static Callable<Object> REQUEST_TASK;
    static Runnable RESPONSE_TASK;

    static String processBusiness(String name) {
        return String.format("%s say hello!", name);
    }

    private static final Map<String,ResponseFuture> RESPONSE_FUTURE_TABLE = Maps.newConcurrentMap();

    @BeforeAll
    public static void beforeClass() {
        String requestId = UUID.randomUUID().toString();
        String requestContent = "wayne";
        REQUEST_TASK = () -> {
           ResponseFuture responseFuture = new ResponseFuture(requestId, 3000);
           RESPONSE_FUTURE_TABLE.put(requestId, responseFuture);
           Thread.sleep(1000);
           log.info("发送请求成功，请求ID:{},请求内容:{}",requestId, requestContent);

           responseFuture.setSendRequestSucceed(true);
           return responseFuture.waitResponse(3000 - 1000);
        };

        RESPONSE_TASK = () -> {
            String responseContent = processBusiness(requestContent);
            try {
                ResponseFuture responseFuture = RESPONSE_FUTURE_TABLE.get(requestId);
                if (null != responseFuture) {
                    log.warn("处理响应成功，请求ID：{}，响应内容：{}", requestId, responseContent);
                    responseFuture.putResponse(responseContent);
                } else {
                    log.warn("请求ID[{}]对应的ResponseFuture不存在，忽略处理",requestId);
                }
            } catch (Exception e) {
                log.info("处理响应失败，请求ID:{},响应内容：{}",requestId,responseContent);
                throw new RuntimeException(e);
            }
        };

        REQUEST_THREAD = Executors.newSingleThreadExecutor(runnable ->{
            Thread thread = new Thread(runnable, "REQUEST_THREAD");
            thread.setDaemon(true);
            return thread;
        });

        NETTY_IO_THREAD = Executors.newSingleThreadExecutor(runnable ->{
            Thread thread = new Thread(runnable, "NETTY_IO_THREAD");
            thread.setDaemon(true);
            return thread;
        });

    }

    @Test
    public void testProcessSync() throws InterruptedException, ExecutionException {
        log.info("异步提交请求处理任务.....");
        Future<Object> future = REQUEST_THREAD.submit(REQUEST_TASK);

        Thread.sleep(1500);
        log.info("异步提交响应处理任务....");
        NETTY_IO_THREAD.execute(RESPONSE_TASK);

        log.info("同步获取请求结果:{}",future.get());
        Thread.sleep(Long.MAX_VALUE);
    }

}
