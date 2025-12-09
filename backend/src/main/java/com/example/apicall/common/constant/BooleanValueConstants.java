package com.example.apicall.common.constant;

/**
 * 布尔值数值常量
 * 用于统一管理系统中使用数值表示的布尔值，避免使用魔法数值
 */
public class BooleanValueConstants {
    
    /** 数值1表示布尔值true */
    public static final Integer TRUE_VALUE = 1;
    
    /** 数值0表示布尔值false */
    public static final Integer FALSE_VALUE = 0;
    
    /**
     * 将Integer数值转换为Boolean类型
     * @param value 数值（1或0）
     * @return 对应的布尔值
     */
    public static Boolean toBoolean(Integer value) {
        return value != null && value == TRUE_VALUE;
    }
    
    /**
     * 将Boolean类型转换为Integer数值
     * @param bool 布尔值
     * @return 对应的数值（1或0）
     */
    public static Integer toInteger(Boolean bool) {
        return bool != null && bool ? TRUE_VALUE : FALSE_VALUE;
    }
}
