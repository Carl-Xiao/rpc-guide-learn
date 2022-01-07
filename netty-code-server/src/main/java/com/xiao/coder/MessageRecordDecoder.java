package com.xiao.coder;

import com.xiao.Header;
import com.xiao.MessageRecord;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author carl-xiao
 * @description 消息解密
 **/
public class MessageRecordDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessageRecord record = new MessageRecord();
        Header header = new Header();
        //读取8个字节的sessionid
        header.setSessionId(byteBuf.readLong());
        //读取一个字节的操作类型
        header.setType(byteBuf.readByte());
        record.setHeader(header);
        //如果byteBuf剩下的长度还有大于4个字节，说明body不为空
        if (byteBuf.readableBytes() > 4) {
            //读取四个字节的长度
            int length = byteBuf.readInt();
            header.setLength(length);
            byte[] contents = new byte[length];
            byteBuf.readBytes(contents, 0, length);
            ByteArrayInputStream bis = new ByteArrayInputStream(contents);
            ObjectInputStream ois = new ObjectInputStream(bis);
            record.setBody(ois.readObject());
            list.add(record);
            System.out.println("序列化出来的结果：" + record);
        } else {
            System.out.println("消息内容为空");
        }
    }
}
