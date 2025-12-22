package com.yuansaas.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 强大的 Jackson JSON 处理工具类
 *
 * <p>本工具类基于 Spring Boot 提供的全局 ObjectMapper，所有操作都不会修改全局配置，
 * 而是通过创建副本来实现线程安全的定制化处理。</p>
 *
 * <p><b>设计原则：</b></p>
 * <ul>
 *   <li>不修改 Spring 全局 ObjectMapper 配置</li>
 *   <li>所有定制化操作都通过副本实现</li>
 *   <li>线程安全，无共享状态</li>
 *   <li>提供丰富的序列化和反序列化功能</li>
 *   <li>支持动态字段过滤和定制化配置</li>
 *   <li>统一的错误处理机制</li>
 * </ul>
 *
 * <p><b>核心功能：</b></p>
 * <ol>
 *   <li>基础序列化和反序列化</li>
 *   <li>美化输出 JSON</li>
 *   <li>动态字段包含/排除过滤</li>
 *   <li>条件过滤序列化</li>
 *   <li>安全 JSON 合并</li>
 *   <li>JSON 有效性验证</li>
 *   <li>创建临时定制化 ObjectMapper</li>
 *   <li>JSON 树操作</li>
 * </ol>
 *
 * @author HTB
 * @version 1.0.0
 * @since 2025-07-22
 */
@Slf4j
@Component
public class JacksonUtil {

    // Spring Boot 管理的全局 ObjectMapper
    private static ObjectMapper globalMapper;

    // 注入的 Spring ObjectMapper 实例
    private final ObjectMapper springObjectMapper;

    @Autowired
    public JacksonUtil(ObjectMapper objectMapper) {
        this.springObjectMapper = objectMapper;
    }

    /**
     * 初始化全局映射器
     *
     * <p>使用 Spring Boot 提供的全局 ObjectMapper 创建一个只读副本，
     * 作为所有工具方法的默认基础配置。</p>
     */
    @PostConstruct
    public void init() {
        // 创建全局 ObjectMapper 的副本
        globalMapper = springObjectMapper.copy();
        log.debug("JacksonUtil initialized with Spring's global ObjectMapper");
    }

    /**
     * 获取只读全局 ObjectMapper 的副本
     *
     * @return ObjectMapper 的独立副本
     */
    private static ObjectMapper getMapper() {
        if (globalMapper == null) {
            throw new IllegalStateException("JacksonUtil not initialized");
        }
        return globalMapper.copy();
    }

    /**
     * 使用 ObjectMapper 副本处理请求（模板方法）
     *
     * <p>所有操作通过此方法获取 ObjectMapper 独立副本，确保线程安全</p>
     *
     * @param processor 处理函数
     * @param <R> 返回类型
     * @return 处理结果
     */
    private static <R> R processWithMapper(Function<ObjectMapper, R> processor) {
        return processor.apply(getMapper());
    }

    /**
     * 对象转 JSON 字符串
     *
     * @param obj 要序列化的对象
     * @return JSON 字符串
     */
    public static String toJson(Object obj) {
        return processWithMapper(mapper -> {
            try {
                return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw handleException("对象转JSON失败", e);
            }
        });
    }

    /**
     * 对象转美化格式的 JSON 字符串
     *
     * @param obj 要序列化的对象
     * @return 美化后的 JSON 字符串
     */
    public static String toPrettyJson(Object obj) {
        return processWithMapper(mapper -> {
            try {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw handleException("对象转美化JSON失败", e);
            }
        });
    }

    /**
     * JSON 字符串转对象
     *
     * @param json JSON 字符串
     * @param clazz 目标对象类型
     * @param <T> 泛型类型
     * @return 反序列化的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return processWithMapper(mapper -> {
            try {
                return mapper.readValue(json, clazz);
            } catch (IOException e) {
                throw handleException("JSON转对象失败", e);
            }
        });
    }

    /**
     * JSON 字符串转复杂泛型对象
     *
     * <p>示例：List<User> users = fromJson(json, new TypeReference<List<User>>(){})</p>
     *
     * @param json JSON 字符串
     * @param typeReference 类型引用
     * @param <T> 泛型类型
     * @return 反序列化的对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        return processWithMapper(mapper -> {
            try {
                return mapper.readValue(json, typeReference);
            } catch (IOException e) {
                throw handleException("JSON转复杂对象失败", e);
            }
        });
    }

    /**
     * JSON 字符串转 List 对象
     *
     * @param json JSON 字符串
     * @param elementClass List 元素类型
     * @param <T> 泛型类型
     * @return 反序列化的 List 对象
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> elementClass) {
        return processWithMapper(mapper -> {
            try {
                return mapper.readValue(json,
                        mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
            } catch (IOException e) {
                throw handleException("JSON转List失败", e);
            }
        });
    }

    /**
     * JSON 字符串转 Map 对象
     *
     * @param json JSON 字符串
     * @param keyClass Map 键类型
     * @param valueClass Map 值类型
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 反序列化的 Map 对象
     */
    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return processWithMapper(mapper -> {
            try {
                return mapper.readValue(json,
                        mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
            } catch (IOException e) {
                throw handleException("JSON转Map失败", e);
            }
        });
    }

    /**
     * 对象深度转换（基于 JSON 的深拷贝）
     *
     * @param source 源对象
     * @param targetClass 目标类型
     * @param <T> 泛型类型
     * @return 转换后的对象
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        return processWithMapper(mapper -> mapper.convertValue(source, targetClass));
    }

    /**
     * 对象深度转换（基于 JSON 的复杂泛型深拷贝）
     *
     * @param source 源对象
     * @param typeReference 类型引用
     * @param <T> 泛型类型
     * @return 转换后的对象
     */
    public static <T> T convert(Object source, TypeReference<T> typeReference) {
        return processWithMapper(mapper -> mapper.convertValue(source, typeReference));
    }

    /**
     * 对象转 Map
     *
     * @param obj 源对象
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 转换后的 Map
     */
    public static <K, V> Map<K, V> toMap(Object obj) {
        return processWithMapper(mapper ->
                mapper.convertValue(obj, new TypeReference<Map<K, V>>() {}));
    }

    /**
     * Map 转对象
     *
     * @param map Map 对象
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 转换后的对象
     */
    public static <T> T fromMap(Map<?, ?> map, Class<T> clazz) {
        return processWithMapper(mapper -> mapper.convertValue(map, clazz));
    }

    /**
     * 带排除字段的序列化
     *
     * <p>将对象序列化为 JSON，排除指定的字段</p>
     *
     * @param obj 要序列化的对象
     * @param fieldsToExclude 要排除的字段名
     * @return JSON 字符串
     */
    public static String toJsonWithExcludeFilter(Object obj, String... fieldsToExclude) {
        return processWithMapper(mapper -> {
            try {
                ObjectMapper tempMapper = mapper.copy();
                SimpleFilterProvider filterProvider = new SimpleFilterProvider();
                filterProvider.addFilter("excludeFilter",
                        SimpleBeanPropertyFilter.serializeAllExcept(fieldsToExclude));
                tempMapper.setFilterProvider(filterProvider);
                return tempMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw handleException("带排除过滤的序列化失败", e);
            }
        });
    }

    /**
     * 带包含字段的序列化
     *
     * <p>将对象序列化为 JSON，仅包含指定的字段</p>
     *
     * @param obj 要序列化的对象
     * @param fieldsToInclude 要包含的字段名
     * @return JSON 字符串
     */
    public static String toJsonWithIncludeFilter(Object obj, String... fieldsToInclude) {
        return processWithMapper(mapper -> {
            try {
                ObjectMapper tempMapper = mapper.copy();
                SimpleFilterProvider filterProvider = new SimpleFilterProvider();
                filterProvider.addFilter("includeFilter",
                        SimpleBeanPropertyFilter.filterOutAllExcept(fieldsToInclude));
                tempMapper.setFilterProvider(filterProvider);
                return tempMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw handleException("带包含过滤的序列化失败", e);
            }
        });
    }

    /**
     * 带条件过滤的序列化
     *
     * <p>将对象序列化为 JSON，根据条件判断是否包含字段</p>
     *
     * @param obj 要序列化的对象
     * @param predicate 字段过滤条件（返回 true 包含，false 排除）
     * @return JSON 字符串
     */
    public static String toJsonWithConditionalFilter(Object obj, Predicate<String> predicate) {
        return processWithMapper(mapper -> {
            try {
                ObjectMapper tempMapper = mapper.copy();
                SimpleFilterProvider filterProvider = new SimpleFilterProvider();
                filterProvider.addFilter("conditionalFilter", createDynamicFilter(predicate));
                tempMapper.setFilterProvider(filterProvider);
                return tempMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw handleException("带条件过滤的序列化失败", e);
            }
        });
    }

    // 创建动态条件过滤器
    private static SimpleBeanPropertyFilter createDynamicFilter(Predicate<String> predicate) {
        return new SimpleBeanPropertyFilter() {
            @Override
            protected boolean include(BeanPropertyWriter writer) {
                return predicate.test(writer.getName());
            }

            @Override
            protected boolean include(PropertyWriter writer) {
                return predicate.test(writer.getName());
            }
        };
    }

    /**
     * JSON 安全合并
     *
     * <p>将源 JSON 合并到目标对象中，返回合并后的新对象</p>
     *
     * <p><b>注意：</b>此操作不会修改原始目标对象</p>
     *
     * @param target 目标对象
     * @param sourceJson 源 JSON 字符串
     * @param <T> 目标对象类型
     * @return 合并后的新对象
     */
    public static <T> T mergeJson(T target, String sourceJson) {
        return processWithMapper(mapper -> {
            try {
                // 深度复制目标对象树
                JsonNode targetNode = mapper.valueToTree(target).deepCopy();
                JsonNode sourceNode = mapper.readTree(sourceJson);
                JsonNode mergedNode = mergeNodes(targetNode, sourceNode);
                return mapper.treeToValue(mergedNode, (Class<T>) target.getClass());
            } catch (IOException e) {
                throw handleException("JSON合并失败", e);
            }
        });
    }

    // 递归合并 JSON 节点
    private static JsonNode mergeNodes(JsonNode target, JsonNode source) {
        if (target == null || source == null) return source != null ? source : target;

        if (target.isObject() && source.isObject()) {
            ObjectNode mergedNode = ((ObjectNode) target).deepCopy();
            source.fields().forEachRemaining(entry -> {
                String field = entry.getKey();
                JsonNode value = entry.getValue();
                JsonNode targetValue = mergedNode.get(field);

                if (targetValue != null && targetValue.isObject() && value.isObject()) {
                    mergedNode.replace(field, mergeNodes(targetValue, value));
                } else if (targetValue != null && targetValue.isArray() && value.isArray()) {
                    mergedNode.replace(field, mergeArrays((ArrayNode) targetValue, (ArrayNode) value));
                } else {
                    mergedNode.replace(field, value.deepCopy());
                }
            });
            return mergedNode;
        } else if (target.isArray() && source.isArray()) {
            return mergeArrays((ArrayNode) target, (ArrayNode) source);
        }
        return source; // 非对象和数组类型直接替换
    }

    // 合并数组
    private static ArrayNode mergeArrays(ArrayNode target, ArrayNode source) {
        ArrayNode merged = target.deepCopy();
        source.forEach(merged::add);
        return merged;
    }

    /**
     * 检查 JSON 字符串有效性
     *
     * @param json 要检查的 JSON 字符串
     * @return 如果有效返回 true，否则返回 false
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.isEmpty()) return false;
        return processWithMapper(mapper -> {
            try {
                mapper.readTree(json);
                return true;
            } catch (IOException e) {
                return false;
            }
        });
    }

    /**
     * 解析 JSON 树
     *
     * @param json JSON 字符串
     * @return JsonNode 对象
     */
    public static JsonNode readTree(String json) {
        return processWithMapper(mapper -> {
            try {
                return mapper.readTree(json);
            } catch (IOException e) {
                throw handleException("解析JSON树失败", e);
            }
        });
    }

    /**
     * 从 JSON 树中提取数据
     *
     * @param json JSON 字符串
     * @param jsonPointer JSON 指针 (RFC 6901)
     * @return 提取的值
     */
    public static JsonNode extractFromJson(String json, String jsonPointer) {
        return processWithMapper(mapper -> {
            try {
                JsonNode root = mapper.readTree(json);
                return root.at(JsonPointer.compile(jsonPointer));
            } catch (IOException e) {
                throw handleException("JSON提取失败", e);
            }
        });
    }

    /**
     * 创建带自定义序列化器的 ObjectMapper
     *
     * @param type 对象类型
     * @param serializer 自定义序列化器
     * @param <T> 泛型类型
     * @return 定制化的 ObjectMapper 副本
     */
    public static <T> ObjectMapper withCustomSerializer(Class<T> type, JsonSerializer<T> serializer) {
        return processWithMapper(mapper -> {
            ObjectMapper customMapper = mapper.copy();
            customMapper.registerModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) {
                    // 正确添加序列化器的方式
                    context.addSerializers(new Serializers.Base() {
                        @Override
                        public JsonSerializer<?> findSerializer(SerializationConfig config,
                                                                JavaType javaType,
                                                                BeanDescription beanDesc) {
                            if (javaType.getRawClass().isAssignableFrom(type)) {
                                return serializer;
                            }
                            return null;
                        }
                    });
                }
            });
            return customMapper;
        });
    }

    /**
     * 创建带自定义反序列化器的 ObjectMapper
     *
     * @param type 对象类型
     * @param deserializer 自定义反序列化器
     * @param <T> 泛型类型
     * @return 定制化的 ObjectMapper 副本
     */
    public static <T> ObjectMapper withCustomDeserializer(Class<T> type, JsonDeserializer<? extends T> deserializer) {
        return processWithMapper(mapper -> {
            ObjectMapper customMapper = mapper.copy();
            customMapper.registerModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) {
                    // 正确添加反序列化器的方式
                    context.addDeserializers(new Deserializers.Base() {
                        @Override
                        public JsonDeserializer<?> findBeanDeserializer(JavaType type,
                                                                        DeserializationConfig config,
                                                                        BeanDescription beanDesc) {
                            if (type.getRawClass().isAssignableFrom(type.getClass())) {
                                return deserializer;
                            }
                            return null;
                        }
                    });
                }
            });
            return customMapper;
        });
    }

    /**
     * 创建带日期格式的 ObjectMapper
     *
     * @param pattern 日期格式模式
     * @return 定制化的 ObjectMapper 副本
     */
    public static ObjectMapper withDateFormat(String pattern) {
        return processWithMapper(mapper -> {
            ObjectMapper customMapper = mapper.copy();
            customMapper.setDateFormat(new SimpleDateFormat(pattern));
            return customMapper;
        });
    }

    /**
     * 创建带 ISO8601 日期格式的 ObjectMapper
     *
     * @return 定制化的 ObjectMapper 副本
     */
    public static ObjectMapper withISO8601DateFormat() {
        return processWithMapper(mapper -> {
            ObjectMapper customMapper = mapper.copy();
            customMapper.setDateFormat(new ISO8601DateFormat());
            return customMapper;
        });
    }

    /**
     * 创建带空值处理策略的 ObjectMapper
     *
     * @param inclusion 包含策略
     * @return 定制化的 ObjectMapper 副本
     */
    public static ObjectMapper withSerializationInclusion(JsonInclude.Include inclusion) {
        return processWithMapper(mapper -> {
            ObjectMapper customMapper = mapper.copy();
            customMapper.setSerializationInclusion(inclusion);
            return customMapper;
        });
    }

    /**
     * 添加序列化修饰器
     *
     * @param modifier 序列化修饰器
     * @return 定制化的 ObjectMapper 副本
     */
    public static ObjectMapper withSerializerModifier(BeanSerializerModifier modifier) {
        return processWithMapper(mapper -> {
            ObjectMapper customMapper = mapper.copy();
            customMapper.registerModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) {
                    context.addBeanSerializerModifier(modifier);
                }
            });
            return customMapper;
        });
    }

    /**
     * 比较两个 JSON 是否相等（忽略顺序）
     *
     * @param json1 第一个 JSON 字符串
     * @param json2 第二个 JSON 字符串
     * @return 如果语义相等返回 true，否则返回 false
     */
    public static boolean jsonEquals(String json1, String json2) {
        return processWithMapper(mapper -> {
            try {
                JsonNode tree1 = mapper.readTree(json1);
                JsonNode tree2 = mapper.readTree(json2);
                return tree1.equals(tree2);
            } catch (IOException e) {
                return false;
            }
        });
    }

    /**
     * 压缩 JSON 字符串（移除不必要的空格）
     *
     * @param json 要压缩的 JSON 字符串
     * @return 压缩后的 JSON
     */
    public static String compressJson(String json) {
        return processWithMapper(mapper -> {
            try {
                JsonNode node = mapper.readTree(json);
                return mapper.writeValueAsString(node);
            } catch (IOException e) {
                return json; // 解析失败返回原始 JSON
            }
        });
    }

    /**
     * 自定义序列化
     *
     * @param obj 要序列化的对象
     * @param serializer 自定义序列化函数
     * @return JSON 字符串
     */
    public static String toJsonWithCustomSerializer(Object obj, Function<Object, String> serializer) {
        return processWithMapper(mapper -> {
            try {
                return serializer.apply(obj);
            } catch (Exception e) {
                throw handleException("自定义序列化失败", e);
            }
        });
    }

    /**
     * 自定义反序列化
     *
     * @param json JSON 字符串
     * @param deserializer 自定义反序列化函数
     * @param <T> 泛型类型
     * @return 反序列化的对象
     */
    public static <T> T fromJsonWithCustomDeserializer(String json, Function<String, T> deserializer) {
        return processWithMapper(mapper -> {
            try {
                return deserializer.apply(json);
            } catch (Exception e) {
                throw handleException("自定义反序列化失败", e);
            }
        });
    }

    /**
     * 统一异常处理
     *
     * @param message 错误消息
     * @param cause 原始异常
     * @return 封装的 JacksonException
     */
    private static JacksonException handleException(String message, Throwable cause) {
        log.error("{}: {}", message, cause.getMessage(), cause);
        return new JacksonException(message, cause);
    }

    /**
     * Jackson 工具类自定义异常
     */
    @Getter
    public static class JacksonException extends RuntimeException {
        private final long timestamp = System.currentTimeMillis();

        public JacksonException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}