package com.jdcloud.sdk.client.sync;

import com.jdcloud.sdk.common.ResponseMessagePacket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ToString
public class ResponseFuture {

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
    private volatile ResponseMessagePacket response;

    private final CountDownLatch latch = new CountDownLatch(1);

    public ResponseFuture( String requestId,long timeoutMilliseconds) {
        this.timeoutMilliseconds = timeoutMilliseconds;
        this.requestId = requestId;
    }

    public boolean timeout() {
        return System.currentTimeMillis() - beginTimestamp > timeoutMilliseconds;
    }

    public ResponseMessagePacket waitResponse(final long timeoutMilliseconds) throws InterruptedException {
        latch.await(timeoutMilliseconds, TimeUnit.MILLISECONDS);
        return response;
    }

    public void putResponse(ResponseMessagePacket response) {
        this.response = response;
        latch.countDown();
    }
}
