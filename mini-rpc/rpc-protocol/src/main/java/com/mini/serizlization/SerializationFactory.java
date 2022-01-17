package com.mini.serizlization;

import com.mini.serizlization.impl.HessianSerialization;

/**
 * @author carl-xiao
 * @description 序列化工厂类
 **/
public class SerializationFactory {
    public static RpcSerialization getRpcSerialization(byte serializationType) {
        SerializationTypeEnum serializationTypeEnum = SerializationTypeEnum.findByType(serializationType);
        switch (serializationTypeEnum) {
            case HESSIAN:
                return new HessianSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal, " + serializationType);
        }
    }
}
