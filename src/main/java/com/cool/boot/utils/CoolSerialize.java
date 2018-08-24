package com.cool.boot.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * serialize tool
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/24
 */
@SuppressWarnings("unchecked")
public final class CoolSerialize {

    private static Map<Class<?>, Schema<?>> cachedSchema;

    static {
        cachedSchema = new ConcurrentHashMap<>();
    }

    private CoolSerialize(){
    }

    /**
     *  serialize init
     */
    public static <T> byte[] serialize(T obj){
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     *  analyze serialize
     */
    public static <T> T deserialize(byte[] data, Class<T> cls){
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null){
            return null;
        }
        T message = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, message, schema);
        return message;
    }

    /**
     *  create schema
     */
    private static <T> Schema<T> getSchema(Class<T> cls){
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null){
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls, schema);
        }
        return schema;
    }

}
