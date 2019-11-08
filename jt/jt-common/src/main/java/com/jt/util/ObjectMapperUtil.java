package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 对象转化为json
     * @param object 对象
     * @return json数据
     */
    public static String toJSON(Object object) {
        String result;
        try {
            result = MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * JSON转化为对象
     * @param json json数据
     * @param target 泛型对象的类对象
     * @param <T>泛型
     * @return 对象
     */
    public static <T> T toObject(String json,Class<T> target) {
        T object;
        try {
            object = MAPPER.readValue(json,target);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return object;
    }
}
