package com.mini.serizlization;

import lombok.Getter;

/**
 * @author carl-xiao
 * @description 序列化
 **/
public enum SerializationTypeEnum {
    HESSIAN(0x10);

    @Getter
    private final int type;

    SerializationTypeEnum(int type) {
        this.type = type;
    }

    /**
     * 序列化方式
     *
     * @param serializationType
     * @return
     */
    public static SerializationTypeEnum findByType(byte serializationType) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.getType() == serializationType) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }

}
