package com.yuansaas.core.context.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * 响应包装器
 *
 * @author HTB 2025/7/24 16:55
 */
public class ResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private ServletOutputStream servletOutputStream;
    private PrintWriter printWriter;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (servletOutputStream == null) {
            servletOutputStream = new CustomServletOutputStream(outputStream);
        }
        return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() {
        if (printWriter == null) {
            // 使用响应的字符编码
            String encoding = getCharacterEncoding();
            if (encoding == null) {
                encoding = Charset.defaultCharset().name();
            }
            printWriter = new PrintWriter(outputStream, false, Charset.forName(encoding));
        }
        return printWriter;
    }

    public byte[] getContentAsByteArray() {
        if (printWriter != null) {
            printWriter.flush();
        }
        return outputStream.toByteArray();
    }

    /**
     * 自定义 ServletOutputStream
     */
    private static class CustomServletOutputStream extends ServletOutputStream {
        private final OutputStream outputStream;

        public CustomServletOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // 不实现
        }
    }
}
