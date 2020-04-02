package com.hupubao.common.wrapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * <h1>请求包装器，为了解决HttpServletRequest不能重复读取InputStream问题</h1>
 * @author ysdxz207
 * @date 2019-09-12
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private byte[] cachedBody;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);


        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return cachedBody.length == 0;
            }
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return new ByteArrayInputStream(cachedBody).read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    public String getBody() {
        return new String(cachedBody, StandardCharsets.UTF_8);
    }

}

