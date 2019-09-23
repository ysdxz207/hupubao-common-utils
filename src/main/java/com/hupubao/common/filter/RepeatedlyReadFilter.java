package com.hupubao.common.filter;

import com.hupubao.common.wrapper.RequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <h1>处理HttpServletRequest向下filter传递</h1>
 * @author ysdxz207
 * @date 2019-09-12 16:11:58
 */
public class RepeatedlyReadFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) request);
        }
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

}
