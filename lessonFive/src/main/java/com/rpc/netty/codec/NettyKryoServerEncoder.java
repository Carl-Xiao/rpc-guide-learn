package com.rpc.netty.codec;

import com.rpc.serialize.Serizlize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * kryo压缩
 */
@AllArgsConstructor
@Slf4j
public class NettyKryoServerEncoder extends MessageToByteEncoder<Object> {

    private Serizlize serizlize;
    private Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        log.info("server encode");
        if (genericClass.isInstance(o)) {
            //1 序列化实体类
            byte[] body = serizlize.serialize(o);
            //2 消息体内容的长度
            int dataLength = body.length;
            // 3.写入消息对应的字节数组长度,writerIndex 加 4
            byteBuf.writeInt(dataLength);
            //4.将字节数组写入 ByteBuf 对象中
            byteBuf.writeBytes(body);
        }
    }
}
