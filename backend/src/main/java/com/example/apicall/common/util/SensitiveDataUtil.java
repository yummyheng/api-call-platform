package com.example.apicall.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import com.example.apicall.common.constant.SensitiveFieldConstants;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 敏感数据处理工具类
 * 用于处理敏感信息，将密码、token等敏感字段值替换为******
 */
@Slf4j
public class SensitiveDataUtil {

    // 敏感信息字段模式
    private static final List<Pattern> SENSITIVE_PATTERNS = Arrays.asList(
            Pattern.compile(String.format(SensitiveFieldConstants.REGEX_FIELD_PATTERN, SensitiveFieldConstants.PASSWORD_FIELD)),
            Pattern.compile(String.format(SensitiveFieldConstants.REGEX_FIELD_PATTERN, SensitiveFieldConstants.TOKEN_FIELD)),
            Pattern.compile(String.format(SensitiveFieldConstants.REGEX_FIELD_PATTERN, SensitiveFieldConstants.SECRET_FIELD)),
            Pattern.compile(String.format(SensitiveFieldConstants.REGEX_FIELD_PATTERN, SensitiveFieldConstants.KEY_FIELD)),
            Pattern.compile(String.format(SensitiveFieldConstants.REGEX_FIELD_PATTERN, SensitiveFieldConstants.ACCESS_ID_FIELD)),
            Pattern.compile(String.format(SensitiveFieldConstants.REGEX_FIELD_PATTERN, SensitiveFieldConstants.ACCESS_KEY_FIELD))
    );

    /**
     * 处理敏感信息，将密码、token等敏感字段值替换为******
     * @param dataMap 包含数据的Map结构
     */
    public static void processSensitiveData(Map<String, Object> dataMap) {
        Optional.ofNullable(dataMap)
                .ifPresent(map -> map.entrySet().forEach(entry -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    // 检查是否为敏感字段
                    boolean isSensitive = SENSITIVE_PATTERNS.stream()
                            .anyMatch(pattern -> pattern.matcher(key).find());

                    if (isSensitive) {
                        // 替换敏感值为******
                        entry.setValue(SensitiveFieldConstants.SENSITIVE_VALUE_REPLACEMENT);
                    } else if (value instanceof Map) {
                        // 递归处理嵌套Map
                        @SuppressWarnings("unchecked")
                        Map<String, Object> nestedMap = (Map<String, Object>) value;
                        processSensitiveData(nestedMap);
                    } else if (value instanceof List) {
                        // 递归处理List中的元素
                        @SuppressWarnings("unchecked")
                        List<Object> nestedList = (List<Object>) value;
                        nestedList.forEach(item -> {
                            if (item instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> itemMap = (Map<String, Object>) item;
                                processSensitiveData(itemMap);
                            } else if (item instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<Object> listItem = (List<Object>) item;
                                processSensitiveData(new HashMap<String, Object>() {{ put("item", listItem); }});
                            }
                        });
                    } else if (value instanceof String) {
                        // 检查字符串值是否包含敏感信息
                        String stringValue = (String) value;
                        processSensitiveString(entry, stringValue);
                    }
                }));
    }

    /**
     * 处理字符串中的敏感信息
     * @param entry Map中的键值对条目
     * @param stringValue 要处理的字符串值
     */
    public static void processSensitiveString(Map.Entry<String, Object> entry, String stringValue) {
        SENSITIVE_PATTERNS.stream()
                .filter(pattern -> pattern.matcher(stringValue).find())
                .findFirst()
                .ifPresent(pattern -> {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> nestedMap = JSON.parseObject(stringValue, new TypeReference<Map<String, Object>>() {});
                        processSensitiveData(nestedMap);
                        entry.setValue(JSON.toJSONString(nestedMap));
                    } catch (Exception e) {
                        // 如果不是JSON格式，不做处理
                        log.debug("字符串值不是JSON格式，跳过敏感信息处理: {}", stringValue);
                    }
                });
    }
}