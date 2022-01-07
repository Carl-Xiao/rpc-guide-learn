package com.xiao;

import lombok.Data;

/**
 * @author carl-xiao
 * @description
 **/
@Data
public class Header {
    //会话id: 占8个字节
    private long sessionId;
    //消息类型：占1个字节
    private byte type;
    //消息长度 : 占4个字节
    private int length;

}
