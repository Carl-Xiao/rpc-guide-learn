package com.xiao.coder;

import com.xiao.Header;
import com.xiao.MessageRecord;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author carl-xiao
 * @description 消息加密
 **/
public class MessageRecordEncoder extends MessageToByteEncoder<MessageRecord> {
    @Override
    public void encode(ChannelHandlerContext ctx, MessageRecord record, ByteBuf byteBuf) throws Exception {
        //消息头
        Header header = record.getHeader();
        //保存8个字节的sessionId
        byteBuf.writeLong(header.getSessionId());
        //写入1个字节的请求类型
        byteBuf.writeByte(header.getType());
        //消息体
        Object body = record.getBody();
        if (body != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(body);
            byte[] bytes = bos.toByteArray();
            //写入消息体长度:占4个字节
            byteBuf.writeInt(bytes.length);
            //写入消息体内容
            byteBuf.writeBytes(bytes);
        } else {
            //写入消息长度占4个字节，长度为0
            byteBuf.writeInt(0);
        }
    }
}
