package com.yuansaas.core.context.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 请求包装器 （支持多次读取请求体）
 *
 * @author HTB 2025/7/24 16:52
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 读取请求体并保存
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public jakarta.servlet.ServletInputStream getInputStream() {
        return new jakarta.servlet.ServletInputStream() {
            private final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(jakarta.servlet.ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() {
                return inputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public byte[] getContentAsByteArray() {
        return body;
    }
}
