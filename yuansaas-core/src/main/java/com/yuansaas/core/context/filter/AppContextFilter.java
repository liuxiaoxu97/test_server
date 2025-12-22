package com.yuansaas.core.context.filter;

import com.yuansaas.core.jackson.JacksonUtil;
import com.yuansaas.core.context.constans.GenericHeaderCons;
import com.yuansaas.core.context.AppContext;
import com.yuansaas.core.context.AppContextHolder;
import com.yuansaas.core.context.TraceIdContext;
import com.yuansaas.core.utils.IpUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * APP 上下文过滤器
 *
 * @author HTB 2025/7/24 10:58
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppContextFilter extends OncePerRequestFilter {

    // TraceId的MDC键名
    private static final String TRACE_ID_MDC_KEY = "traceId";

    @Value("${ys.app.logging.request.enable:true}")
    private boolean logRequestEnabled ;

    @Value("${ys.app.logging.response.enable:false}")
    private boolean logResponseEnabled;

    @Value("${ys.app.logging.request.body.enable:false}")
    private boolean logRequestBodyEnabled ;

    @Value("${ys.app.logging.response.body.enable:false}")
    private boolean logResponseBodyEnabled;

    // 默认 10KB
    @Value("${ys.app.logging.request.body.max-size:10240}")
    private int maxRequestBodySize ;

    // 默认 10KB
    @Value("${ys.app.logging.response.body.max-size:10240}")
    private int maxResponseBodySize ;

    // 敏感字段
    @Value("${ys.app.logging.sensitive.fields:password,password_confirmation}")
    private String sensitiveFields ;

    // 慢请求阈值 500ms
    @Value("${ys.app.logging.slow.threshold:500}")
    private long slowRequestThreshold ;

    // 匹配的请求路径 多路径用逗号隔开，支持api/**， api/user/info
    @Value("${ys.app.logging.request.body.include-patterns:/**}")
    private String requestBodyIncludePatterns ;

    // 匹配的请求路径 多路径用逗号隔开，支持api/**， api/user/info
    @Value("${ys.app.logging.response.body.include-patterns:/**}")
    private String responseBodyIncludePatterns ;

    // 敏感字段
    private Set<String> sensitiveFieldsSet ;

    // 路径匹配器
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private List<String> requestBodyIncludePaths ;
    private List<String> responseBodyIncludePaths ;

    @PostConstruct
    public void ini() {
         // 解析敏感字段
        sensitiveFieldsSet = Set.of(sensitiveFields.split(","));

        // 初始化路径匹配列表
        requestBodyIncludePaths = parsePathPatterns(requestBodyIncludePatterns);
        responseBodyIncludePaths = parsePathPatterns(responseBodyIncludePatterns);

        log.info("AppContextFilter initialized with: \n");
        log.info(" Request body include paths: {}", requestBodyIncludePaths);
        log.info(" Response body include paths: {}", responseBodyIncludePaths);
        log.info(" Sensitive fields: {}", sensitiveFieldsSet);

    }

    /**
     * 解析路径模式
     */
    private List<String> parsePathPatterns(String patterns) {
        if (patterns == null || patterns.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(patterns.split(","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 创建上下文
        AppContext appContext = createAppContext(request);
        // 设置Trace Id
        setupTraceId(appContext);

        // 设置到当前线程
        AppContextHolder.setContext(appContext);

        // 记录请求开始时间
        Instant startTime = Instant.now();
        // 创建包装请求和响应
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        try {
            // 请求日志记录
             printLogRequest(requestWrapper);
            // 继续处理请求
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {

            // 记录响应日志
            printLogResponse(requestWrapper, responseWrapper, startTime);

            // 性能监控
            monitorPerformance(requestWrapper, startTime);

            // 将响应写入到输出流
            writeResponse(response, responseWrapper);

            // 清理当前线程的APP上下文
            cleanupContext();
        }
    }

    private AppContext createAppContext(HttpServletRequest request) {
        return AppContext.create()
                .setClientType(request.getHeader(GenericHeaderCons.CLIENT_TYPE))
                .setIpAddress(request.getRemoteAddr())
                .setSessionId(request.getSession(false) != null ? request.getSession(false).getId() : null);
    }

    /**
     *  设置TraceId到MDC
     * @param appContext APP上下文s
     */
    private void setupTraceId(AppContext appContext) {
        TraceIdContext.start();
        String traceId = TraceIdContext.getTraceId();
        appContext.setTraceId(traceId);
        MDC.put(TRACE_ID_MDC_KEY, traceId);
    }

    private void clearTraceId() {
        MDC.remove(TRACE_ID_MDC_KEY);
    }

    /**
     * 打印请求日志
     */
    private void printLogRequest(RequestWrapper request) {
        if (!logRequestEnabled) return;

        try {
            Map<String , Object> logMap = new HashMap<>();
            logMap.put("logType" , "request");
            logMap.put("method", request.getMethod());
            logMap.put("uri", request.getRequestURI());
            logMap.put("query", formatQueryString(request.getQueryString()));
            logMap.put("remoteAddr" , request.getRemoteAddr());
            logMap.put("realId", IpUtil.getClientIpAddr( request));
            logMap.put("userAgent" , request.getHeader(GenericHeaderCons.USER_AGENT));
            logMap.put("clientType" , request.getHeader(GenericHeaderCons.CLIENT_TYPE));

            // 请求头(过滤铭感信息)
            Map<String , String> headers = new HashMap<>();
            Collections.list(request.getHeaderNames())
                    .forEach(headerName -> {
                        String headerValue = request.getHeader(headerName);
                        headers.put(headerName, maskSensitiveData(headerName, headerValue));
                    });
            logMap.put("headers", headers);
            // 检查是否应记录请求体
            boolean shouldLogRequestBody = logRequestBodyEnabled &&
                    matchesPath(request, requestBodyIncludePaths);

            // 请求体（如果启用）
            if (shouldLogRequestBody) {
                String requestBody = getRequestBody(request);
                logMap.put("body", requestBody);
            } else if (!logRequestBodyEnabled) {
                logMap.put("body", "<SKIPPED>");
            }
            log.info("\n {}", JacksonUtil.toJson(logMap));
        } catch (Exception e) {
            log.error("Error printing request log", e);
        }
    }

    /**
     * 记录响应日志
     */
    private void printLogResponse(RequestWrapper request, ResponseWrapper response, Instant startTime) {
        if (!logResponseEnabled) return;

        try {
            // 计算耗时
            long duration = Duration.between(startTime, Instant.now()).toMillis();

            Map<String, Object> logData = new LinkedHashMap<>();
            logData.put("logType", "response");
            // 基本信息
            logData.put("status", response.getStatus());
            logData.put("method", request.getMethod());
            logData.put("uri", request.getRequestURI());
            logData.put("endpoint", getEndpoint(request));
            logData.put("classMethod", getClassMethod(request));
            logData.put("duration", duration + "ms");

            // 响应头（过滤敏感信息）
            Map<String, String> headers = new LinkedHashMap<>();
            response.getHeaderNames().forEach(headerName -> {
                String headerValue = response.getHeader(headerName);
                headers.put(headerName, maskSensitiveData(headerName, headerValue));
            });
            logData.put("headers", headers);

            // 检查是否应记录响应体
            boolean shouldLogResponseBody = logResponseBodyEnabled &&
                    matchesPath(request, responseBodyIncludePaths);

            // 响应体（如果启用）
            if (shouldLogResponseBody) {
                String responseBody = getResponseBody(response);
                logData.put("body", responseBody);
            } else if (!logResponseBodyEnabled) {
                logData.put("body", "<SKIPPED>");
            }

            // 记录响应日志
            log.info("\n {}", JacksonUtil.toJson( logData));
        } catch (Exception e) {
            logger.error("Error printing response log", e);
        }

    }

    /**
     * 格式化查询字符串（解码、过滤敏感信息、美化）
     */
    private String formatQueryString(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return "";
        }

        // 1. 解码
        String decoded = decodeQueryString(queryString);

        // 2. 过滤敏感信息
        String filtered = filterSensitiveParams(decoded);

        // 3. 美化格式
        return Arrays.stream(filtered.split("&"))
                .map(param -> {
                    String[] parts = param.split("=", 2);
                    if (parts.length == 1) {
                        return parts[0] + ": ";
                    }
                    return parts[0] + ": " + parts[1];
                })
                .collect(Collectors.joining(", "));
    }

    /**
     * 解码查询字符串
     */
    private String decodeQueryString(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return queryString;
        }

        try {
            return URLDecoder.decode(queryString, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return queryString;
        }
    }

    /**
     * 过滤敏感参数
     */
    private String filterSensitiveParams(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return queryString;
        }

        return Arrays.stream(queryString.split("&"))
                .map(param -> {
                    String[] parts = param.split("=", 2);
                    if (parts.length < 2) return param;

                    String name = parts[0];

                    if (sensitiveFieldsSet.contains(name.toLowerCase())) {
                        return name + "=******";
                    }
                    return param;
                })
                .collect(Collectors.joining("&"));
    }

    /**
     * 检查是否匹配路径
     */
    private boolean matchesPath(HttpServletRequest request, List<String> patterns) {
        if (patterns.isEmpty()) return false;

        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();

        // 移除上下文路径
        String path = requestUri.substring(contextPath.length());

        for (String pattern : patterns) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 屏蔽敏感数据（请求头）
     */
    private String maskSensitiveData(String headerName, String headerValue) {
        if (headerValue == null) return null;

        // 屏蔽敏感头信息
        if (sensitiveFieldsSet.contains(headerName.toLowerCase())) {
            return "******";
        }

        return headerValue;
    }

    /**
     * 屏蔽敏感数据
     */
    private String maskSensitiveData(String body) {
        if (body == null || body.isEmpty()) return body;

        // 如果是JSON格式，屏蔽敏感字段
        if (body.startsWith("{") || body.startsWith("[")) {
            return maskJsonSensitiveData(body);
        }

        // 处理表单格式的敏感数据
        if (body.contains("=") && body.contains("&")) {
            return maskFormSensitiveData(body);
        }

        return body;
    }
    /**
     * 屏蔽JSON格式的敏感数据
     */
    private String maskJsonSensitiveData(String body) {
        // 构建正则表达式
        Pattern pattern = Pattern.compile(
                "\"(" + String.join("|", sensitiveFieldsSet) + ")\"\\s*:\\s*\"(.*?)\"",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(body);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            // 保留字段名，屏蔽值
            matcher.appendReplacement(result, "\"" + matcher.group(1) + "\":\"******\"");
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 屏蔽表单格式的敏感数据
     */
    private String maskFormSensitiveData(String body) {
        Pattern pattern = Pattern.compile(
                "(" + String.join("|", sensitiveFieldsSet) + ")=([^&]*)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(body);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(1) + "=******");
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 屏蔽非字符串值的敏感字段
     */
    private String maskNonStringSensitiveData(String body) {
        // 匹配非字符串值的敏感字段（如数字、布尔值）
        Pattern pattern = Pattern.compile(
                "\"(" + String.join("|", sensitiveFieldsSet) + ")\"\\s*:\\s*([^,}\\]]*)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(body);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(result, "\"" + matcher.group(1) + "\":\"******\"");
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 将响应写回原始响应
     */
    private void writeResponse(ServletResponse response, ResponseWrapper responseWrapper) throws IOException {
        byte[] responseBody = responseWrapper.getContentAsByteArray();
        if (responseBody.length > 0) {
            response.getOutputStream().write(responseBody);
        }
    }

    /**
     * 清理上下文
     */
    private void cleanupContext() {
        // 清除上下文
        AppContextHolder.clearContext();
        // 清理当前线程的APP上下文
        clearTraceId();
    }

    /**
     * 记录错误日志
     */
    private void logError(RequestWrapper request, Exception e) {
        Map<String, Object> errorData = new LinkedHashMap<>();
        errorData.put("method", request.getMethod());
        errorData.put("uri", request.getRequestURI());
        errorData.put("error", e.getClass().getSimpleName());
        errorData.put("message", e.getMessage());

        log.error("Request error: {}", errorData, e);
    }

    /**
     * 性能监控
     */
    private void monitorPerformance(RequestWrapper request, Instant startTime) {
        long duration = Duration.between(startTime, Instant.now()).toMillis();

        // 记录慢请求
        if (duration > slowRequestThreshold) {
            log.info("Slow request detected: {}ms - {} {}",
                    duration, request.getMethod(), request.getRequestURI());
        }

        // 记录性能指标
        log.debug("Request processed: {}ms - {} {}",
                duration, request.getMethod(), request.getRequestURI());
    }

    /**
     * 获取请求体
     */
    private String getRequestBody(RequestWrapper request) {
        try {
            byte[] content = request.getContentAsByteArray();
            if (content.length == 0) return "";

            // 限制大小
            if (content.length > maxRequestBodySize) {
                return "<REQUEST-BODY-TOO-LARGE>";
            }

            String body = new String(content, request.getCharacterEncoding());
            return maskSensitiveData(body);
        } catch (Exception e) {
            return "<ERROR-READING-REQUEST-BODY>";
        }
    }

    /**
     * 获取响应体
     */
    private String getResponseBody(ResponseWrapper response) {
        try {
            byte[] content = response.getContentAsByteArray();
            if (content.length == 0) return "";

            // 限制大小
            if (content.length > maxResponseBodySize) {
                return "<RESPONSE-BODY-TOO-LARGE>";
            }

            String body = new String(content, response.getCharacterEncoding());
            return maskSensitiveData(body);
        } catch (Exception e) {
            return "<ERROR-READING-RESPONSE-BODY>";
        }
    }

    /**
     * 获取端点标识
     */
    private String getEndpoint(HttpServletRequest request) {
        try {
            // 尝试从HandlerMapping获取端点信息
            return String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        } catch (Exception e) {
            // 忽略错误
            log.error("Error getting endpoint", e);
        }

        // 回退到URI
        return request.getRequestURI();
    }

    /**
     * 获取端点标识
     */
    private String getClassMethod(HttpServletRequest request) {
        try {
            // 尝试从HandlerMapping获取端点信息
            Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

            if (handler instanceof HandlerMethod handlerMethod) {
                return handlerMethod.getBeanType().getSimpleName() + "#" + handlerMethod.getMethod().getName();
            }
        } catch (Exception e) {
            // 忽略错误
            log.error("Error getting class method", e);
        }

        // 回退到URI
        return request.getRequestURI();
    }
}
