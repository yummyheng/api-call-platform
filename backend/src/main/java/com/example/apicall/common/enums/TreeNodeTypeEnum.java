package com.example.apicall.common.enums;

/**
 * 树节点类型枚举
 * 用于统一管理系统中树形结构的节点类型，避免使用魔法字符串
 */
public enum TreeNodeTypeEnum {
    
    /** 根节点 */
    ROOT("root"),
    
    /** 项目节点 */
    PROJECT("project"),
    
    /** 请求节点 */
    REQUEST("request"),
    
    /** 测试用例节点 */
    TEST_CASE("test_case");
    
    private final String value;
    
    TreeNodeTypeEnum(String value) {
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
    public static TreeNodeTypeEnum fromValue(String value) {
        for (TreeNodeTypeEnum type : TreeNodeTypeEnum.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
