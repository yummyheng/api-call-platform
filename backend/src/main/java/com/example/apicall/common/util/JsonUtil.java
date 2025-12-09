package com.example.apicall.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

/**
 * JSON处理工具类
 * 提供JSON字符串与对象之间的转换等通用方法
 */
@Slf4j
public class JsonUtil {

    /**
     * 解析JSON字符串为Map结构
     * @param jsonString JSON格式的字符串
     * @return 解析后的Map结构，解析失败返回Optional.empty()
     */
    public static Optional<Map<String, Object>> parseJson(String jsonString) {
        try {
            return Optional.of(JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {}));
        } catch (Exception e) {
            log.debug("解析JSON失败: {}", jsonString, e);
            return Optional.empty();
        }
    }

    /**
     * 将对象转换为JSON字符串
     * @param object 要转换的对象
     * @return JSON格式的字符串，转换失败返回null
     */
    public static String toJsonString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.error("对象转换为JSON失败: {}", object, e);
            return null;
        }
    }

    /**
     * 解析JSON字符串为指定类型的对象
     * @param jsonString JSON格式的字符串
     * @param typeReference 类型引用
     * @param <T> 目标类型
     * @return 解析后的对象，解析失败返回Optional.empty()
     */
    public static <T> Optional<T> parseJson(String jsonString, TypeReference<T> typeReference) {
        try {
            return Optional.of(JSON.parseObject(jsonString, typeReference));
        } catch (Exception e) {
            log.debug("解析JSON为指定类型失败: {}", jsonString, e);
            return Optional.empty();
        }
    }

    /**
     * 将JSON字符串解析为指定类的实例
     * @param jsonString JSON格式的字符串
     * @param clazz 目标类
     * @param <T> 目标类型
     * @return 解析后的对象，解析失败返回Optional.empty()
     */
    public static <T> Optional<T> parseJson(String jsonString, Class<T> clazz) {
        try {
            return Optional.of(JSON.parseObject(jsonString, clazz));
        } catch (Exception e) {
            log.debug("解析JSON为指定类失败: {}", jsonString, e);
            return Optional.empty();
        }
    }
}