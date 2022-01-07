package com.xiao;

import lombok.Data;

/**
 * @author carl-xiao
 * @description
 **/
@Data
public class MessageRecord {
    private Header header;
    private Object body;
}
