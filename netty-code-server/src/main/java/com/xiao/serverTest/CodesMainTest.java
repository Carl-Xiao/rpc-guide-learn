package com.xiao.serverTest;

import com.xiao.Header;
import com.xiao.MessageRecord;
import com.xiao.OpCode;
import com.xiao.coder.MessageRecordDecoder;
import com.xiao.coder.MessageRecordEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author carl-xiao
 * @description
 **/
public class CodesMainTest {
    public static void main(String[] args) throws Exception {

        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 9, 4, 0, 0),
                new LoggingHandler(),
                new MessageRecordEncoder(),
                new MessageRecordDecoder());
        //组装消息
        Header header = new Header();
        header.setSessionId(123456);
        header.setType(OpCode.PING.code());
        MessageRecord record = new MessageRecord();
        record.setBody("Hello World");
        record.setHeader(header);
        channel.writeOutbound(record);
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageRecordEncoder().encode(null, record, buf);
//        channel.writeInbound(buf);
        //获取前面7个字节
        ByteBuf bb1 = buf.slice(0, 7);
        //获取后面的字节
        ByteBuf bb2 = buf.slice(7, buf.readableBytes() - 7);
        bb1.retain();
        channel.writeInbound(bb1);
        channel.writeInbound(bb2);
    }
}
