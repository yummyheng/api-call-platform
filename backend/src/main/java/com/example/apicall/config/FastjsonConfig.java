package com.example.apicall.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FastjsonConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建Fastjson消息转换器
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        // 设置Fastjson配置
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        // 配置序列化特性
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteNullListAsEmpty, // 空列表返回[]而不是null
                SerializerFeature.WriteNullStringAsEmpty, // 空字符串返回""而不是null
                SerializerFeature.WriteMapNullValue, // 输出所有字段，包括null值
                SerializerFeature.WriteDateUseDateFormat, // 日期类型格式化为"yyyy-MM-dd HH:mm:ss"
                SerializerFeature.DisableCircularReferenceDetect // 禁用循环引用检测
        );
        // 设置默认编码
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        // 设置支持的MediaType
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonConverter.setSupportedMediaTypes(mediaTypes);
        // 应用配置到消息转换器
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);
        // 将fastjson添加到消息转换器列表并放在首位
        converters.add(0, fastJsonConverter);
    }
}