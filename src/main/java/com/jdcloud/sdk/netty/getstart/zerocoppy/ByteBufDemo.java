package com.jdcloud.sdk.netty.getstart.zerocoppy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class ByteBufDemo {

    public static void main(String[] args) {
        ByteBufDemo demo = new ByteBufDemo();
        demo.compositeDeom();
    }
    public void compositeDeom() {
        ByteBuf buf1 = PooledByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes("heap test.".getBytes());

        ByteBuf buf2 = PooledByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes("direct test.".getBytes());

        ByteBuf compositeByteBuf = Unpooled.wrappedBuffer(buf1, buf2);

        byte[] readBytes = new byte[compositeByteBuf.readableBytes()];
        compositeByteBuf.readBytes(readBytes);
        System.out.println("read content: " + new String(readBytes));

        ByteBuf buf3 = buf1.slice(0, 4);
        byte[] readBytes2 = new byte[buf3.readableBytes()];
        buf3.readBytes(readBytes2);
        System.out.println("read content: "+ new String(readBytes2));


    }
}
