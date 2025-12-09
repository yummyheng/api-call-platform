package com.example.apicall.common.enums;

/**
 * 节点类型枚举
 * 用于统一管理API树节点的类型，避免使用魔法字符串
 */
public enum NodeTypeEnum {
    
    /** 根节点 */
    ROOT("root"),
    
    /** 测试用例节点 */
    TEST_CASE("test_case"),
    
    /** API请求节点 */
    REQUEST("request"),
    
    /** 项目节点 */
    PROJECT("project");
    
    private final String value;
    
    NodeTypeEnum(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 根据字符串值获取枚举
     * @param value 字符串值
     * @return 对应的枚举，如果没有匹配返回null
     */
    public static NodeTypeEnum fromValue(String value) {
        for (NodeTypeEnum type : NodeTypeEnum.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}