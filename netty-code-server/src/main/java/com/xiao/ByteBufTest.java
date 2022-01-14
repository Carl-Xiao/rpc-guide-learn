package com.xiao;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author carl-xiao
 * @description
 **/
public class ByteBufTest {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(6, 10);
//        printByteBufInfo("ByteBufAllocator.buffer(5, 10)", buffer);
        //写入两个字节数据
        buffer.writeBytes(new byte[]{1, 2});
//        printByteBufInfo("write 2 Bytes", buffer);
        //写入int数据 一个int4个字节
        buffer.writeInt(1000);
//        printByteBufInfo("write Int 100", buffer);
        //写入3个字节
        buffer.writeBytes(new byte[]{3, 4, 5});
//        printByteBufInfo("write 3 Bytes", buffer);
        //读数据
//        byte[] read = new byte[buffer.readableBytes()];
//        buffer.readBytes(read);
//        printByteBufInfo("readBytes(" + buffer.readableBytes() + ")", buffer);
        System.out.println("getInt(2): " + buffer.getInt(2));
        System.out.println("getInt(2): " + buffer.getByte(6));
        buffer.setByte(1, 0);
        System.out.println("getByte(1): " + buffer.getByte(1));
//        printByteBufInfo("AfterGetAndSet", buffer);
    }

    private static void printByteBufInfo(String step, ByteBuf buffer) {
        System.out.println("------" + step + "-----");

        System.out.println("readerIndex(): " + buffer.readerIndex());

        System.out.println("writerIndex(): " + buffer.writerIndex());

        System.out.println("isReadable(): " + buffer.isReadable());

        System.out.println("isWritable(): " + buffer.isWritable());

        System.out.println("readableBytes(): " + buffer.readableBytes());

        System.out.println("writableBytes(): " + buffer.writableBytes());

        System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());

        System.out.println("capacity(): " + buffer.capacity());

        System.out.println("maxCapacity(): " + buffer.maxCapacity());
    }

}
