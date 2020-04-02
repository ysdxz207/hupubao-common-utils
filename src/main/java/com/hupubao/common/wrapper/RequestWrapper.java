package com.hupubao.common.wrapper;

import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * <h1>请求包装器，为了解决HttpServletRequest不能重复读取InputStream问题</h1>
 * @author ysdxz207
 * @date 2019-09-12
 */
public class RequestWrapper extends ContentCachingRequestWrapper {

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String getBody() {
        return new String(getContentAsByteArray(), StandardCharsets.UTF_8);
    }
}

