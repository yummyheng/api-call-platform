package com.example.apicall.utils;

import com.example.apicall.dto.ApiEnvironmentDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换为JSON字符串
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }

    /**
     * 将JSON字符串转换为对象
     * @param json JSON字符串
     * @param clazz 对象类型
     * @param <T> 对象类型
     * @return 对象
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to object", e);
        }
    }

    /**
     * 将JSON字符串转换为KeyPairDTO列表
     * @param json JSON字符串
     * @return KeyPairDTO列表
     */
    public static List<ApiEnvironmentDTO.KeyPairDTO> toKeyPairList(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            List<Map<String, String>> mapList = objectMapper.readValue(json, List.class);
            List<ApiEnvironmentDTO.KeyPairDTO> keyPairList = new ArrayList<>();
            for (Map<String, String> map : mapList) {
                ApiEnvironmentDTO.KeyPairDTO keyPair = new ApiEnvironmentDTO.KeyPairDTO();
                keyPair.setKey(map.get("key"));
                keyPair.setValue(map.get("value"));
                keyPairList.add(keyPair);
            }
            return keyPairList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to KeyPairDTO list", e);
        }
    }
}