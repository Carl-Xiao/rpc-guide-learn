package com.rpc.netty;

import com.rpc.serialize.Serizlize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * kryo解压
 */
@AllArgsConstructor
@Slf4j
public class NettyKryoServerDecoder extends ByteToMessageDecoder {
    private Serizlize serizlize;
    private Class<?> genericClass;

    private static final int BODY_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("server decode");
        if (byteBuf.readableBytes() >= BODY_LENGTH) {
            //标记当前readIndex的位置, 以便后面重置readIndex的使用
            byteBuf.markReaderIndex();
            //读消息体
            int dataLength = byteBuf.readInt();
            //4.消息体为空直接返回
            if (dataLength < 0 || byteBuf.readableBytes() < 0) {
                return;
            }
            //可读字节数小于消息长度的话，说明是不完整的消息，重置readIndex
            if (byteBuf.readableBytes() < dataLength) {
                byteBuf.resetReaderIndex();
                return;
            }
            // 开始序列化
            byte[] body = new byte[dataLength];
            byteBuf.readBytes(body);
            Object obj = serizlize.deserialize(body, genericClass);
            list.add(obj);
        }
    }
}
