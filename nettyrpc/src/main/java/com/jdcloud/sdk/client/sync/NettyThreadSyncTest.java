package com.jdcloud.sdk.client.sync;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

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

    @BeforeTestClass
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


        };
    }


}
