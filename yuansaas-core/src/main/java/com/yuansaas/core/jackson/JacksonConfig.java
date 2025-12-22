package com.yuansaas.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 配置类
 *
 * <p>优化点：
 * 1. 使用明确的序列化/反序列化类
 * 2. 修复 Lambda 表达式中的类型问题
 * 3. 保持代码简洁的同时确保类型安全
 * 4. 添加完整的异常处理
 * 5. 增强可读性和可维护性</p>
 *
 * @author HTB 2025/7/22 16:24
 */
@Configuration
public class JacksonConfig {

    // 日期时间格式常量
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final Environment environment;

    @Autowired
    public JacksonConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 基础配置
        configureBasicFeatures(mapper);

        // 日期时间处理
        configureDateTimeHandling(mapper);

        // 环境相关配置
        configureEnvironmentSpecificSettings(mapper);

        return mapper;
    }

    /**
     * 配置基础序列化/反序列化特性
     */
    private void configureBasicFeatures(ObjectMapper mapper) {
        // 反序列化配置
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // 序列化配置
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);

        // 空值处理策略
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // 全局配置所有 Long 类型序列化为字符串
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class , ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(module);
    }

    /**
     * 配置日期时间处理
     */
    private void configureDateTimeHandling(ObjectMapper mapper) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // LocalDate 序列化/反序列化
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        // LocalTime 序列化/反序列化
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());

        // LocalDateTime 序列化/反序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        mapper.registerModule(javaTimeModule);
    }

    /**
     * 配置环境相关设置
     */
    private void configureEnvironmentSpecificSettings(ObjectMapper mapper) {
        // 开发环境美化输出
        if (isDevEnvironment()) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        }

        // 生产环境优化配置
        if (isProdEnvironment()) {
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
    }

    /**
     * 判断是否为开发环境
     */
    private boolean isDevEnvironment() {
        // todo 环境配置
        return environment.acceptsProfiles("dev", "test", "local");
    }

    /**
     * 判断是否为生产环境
     */
    private boolean isProdEnvironment() {
        // todo 环境配置
        return environment.acceptsProfiles("prod", "production");
    }

    // ======================== 日期时间序列化/反序列化类 ========================

    /**
     * LocalDate 序列化器
     */
    private static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        }
    }

    /**
     * LocalDate 反序列化器
     */
    private static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser parser, DeserializationContext context)
                throws IOException, JacksonException {
            return LocalDate.parse(parser.getText(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        }
    }

    /**
     * LocalTime 序列化器
     */
    private static class LocalTimeSerializer extends JsonSerializer<LocalTime> {
        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        }
    }

    /**
     * LocalTime 反序列化器
     */
    private static class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser parser, DeserializationContext context)
                throws IOException, JacksonException {
            return LocalTime.parse(parser.getText(), DateTimeFormatter.ofPattern(TIME_FORMAT));
        }
    }

    /**
     * LocalDateTime 序列化器
     */
    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        }
    }

    /**
     * LocalDateTime 反序列化器
     */
    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
                throws IOException, JacksonException {
            return LocalDateTime.parse(parser.getText(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        }
    }
}