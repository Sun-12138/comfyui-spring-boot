package com.comfyui.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * @author Sun_12138
 */
public class JsonUtils {

    /**
     * ObjectMapper对象
     */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    /**
     * 将字符串转为对应的对象
     *
     * @param json      json字符串
     * @param valueType 对象类型
     * @param <T>       对象类型
     * @return 转换后的对象
     */
    public static <T> T toObject(JsonNode json, Class<T> valueType) {
        return toObject(json.toString(), valueType);
    }

    /**
     * 将字符串转为对应的对象
     *
     * @param json      json字符串
     * @param valueType 对象类型
     * @param <T>       对象类型
     * @return 转换后的对象
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        T t;
        try {
            t = OBJECT_MAPPER.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * 将json转为map
     *
     * @param node      JsonNode对象
     * @param valueType 字典value的类型
     * @param <T>       map中value的类型
     * @return map对象
     */
    public static <T> Map<String, T> toMapObject(JsonNode node, Class<T> valueType) {
        return toMapObject(node.toString(), valueType);
    }

    /**
     * 将json转为map
     *
     * @param json      JsonNode对象
     * @param valueType 字典value的类型
     * @param <T>       map中value的类型
     * @return map对象
     */
    public static <T> Map<String, T> toMapObject(String json, Class<T> valueType) {
        Map<String, T> t;
        try {
            t = OBJECT_MAPPER.readerForMapOf(valueType).readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * 将json转为list
     *
     * @param node      JsonNode对象
     * @param valueType list中元素的类型
     * @param <T>       list中元素的类型
     * @return list对象
     */
    public static <T> List<T> toListObject(JsonNode node, Class<T> valueType) {
        return toListObject(node.toString(), valueType);
    }

    /**
     * 将json转为list
     *
     * @param json      JsonNode对象
     * @param valueType list中元素的类型
     * @param <T>       list中元素的类型
     * @return list对象
     */
    public static <T> List<T> toListObject(String json, Class<T> valueType) {
        List<T> t;
        try {
            t = OBJECT_MAPPER.readerForListOf(valueType).readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    /**
     * Json转JsonNode对象
     *
     * @param json json字符串
     * @return JsonNode对象
     */
    public static JsonNode toJsonNode(String json) {
        JsonNode node;
        try {
            node = OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return node;
    }

    /**
     * 对象转JsonNode对象
     *
     * @param object 目标对象
     * @return JsonNode对象
     */
    public static JsonNode toJsonNode(Object object) {
        return OBJECT_MAPPER.valueToTree(object);
    }

    /**
     * 对象转json字符串
     *
     * @param object 目标对象
     * @return json字符串
     */
    public static String toJsonString(Object object) {
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
