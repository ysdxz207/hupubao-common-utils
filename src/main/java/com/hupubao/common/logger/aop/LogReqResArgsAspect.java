package com.hupubao.common.logger.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hupubao.common.logger.annotations.LogReqResArgs;
import com.hupubao.common.utils.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @author ysdxz207
 * @date 2018-06-11 14:05:53
 * 请求，返回参数日志记录工具
 */
@Aspect
@Component
public class LogReqResArgsAspect {
    /**
     * 需要有构造方法
     * 否则会报Caused by: java.lang.NoSuchMethodError xxx  method <init>()V not found
     */
    public LogReqResArgsAspect() {
    }


    /**
     * 请求日志记录
     *
     * @param joinPoint
     * @param logReqResArgs
     */
    @Before("execution(* *(..)) && @annotation(logReqResArgs)")
    public void before(JoinPoint joinPoint,
                       LogReqResArgs logReqResArgs) {

        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
            doLog(joinPoint, logReqResArgs, request);
        } else {
            LoggerUtils.warn(getClass(), "Method [" + getMethod(joinPoint).getName() + "] with annotation [LogReqResArgs] should have HttpServletRequest type parameters.");
        }
    }

    /**
     * 响应日志记录
     *
     * @param joinPoint
     * @param logReqResArgs
     */
    @AfterReturning(pointcut = "execution(* *(..)) && @annotation(logReqResArgs)", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint,
                               LogReqResArgs logReqResArgs,
                               Object returnValue) {
        doLog(joinPoint, logReqResArgs, returnValue);
    }

    /**
     * 异常日志记录
     *
     * @param joinPoint
     * @param logReqResArgs
     * @param throwable
     */
    @AfterThrowing(pointcut = "execution(* *(..)) && @annotation(logReqResArgs)", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint,
                              LogReqResArgs logReqResArgs,
                              Throwable throwable) {
        if (logReqResArgs.logException()) {
            doLog(joinPoint, logReqResArgs, throwable);
        }
    }

    /**
     * 公共记录日志方法
     *
     * @param joinPoint
     * @param logReqResArgs
     * @param info
     */
    private void doLog(JoinPoint joinPoint,
                       LogReqResArgs logReqResArgs,
                       Object info) {
        Method targetMethod = getMethod(joinPoint);
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append(StringUtils.isNotBlank(logReqResArgs.title()) ? logReqResArgs.title() : getDefaultTitle(targetMethod));
        sb.append("]");

        if (logReqResArgs.logMethod()) {
            sb.append("[");
            sb.append(getHttpServletRequest().getMethod());
            sb.append("]");
        }

        if (logReqResArgs.logUrl()) {
            sb.append("[");
            sb.append(getHttpServletRequest().getRequestURL());
            sb.append("]");
        }

        if (info instanceof Throwable) {
            sb.append(logReqResArgs.titleException());
            LoggerUtils.info(targetMethod.getDeclaringClass(), sb.toString(), (Throwable) info);
            return;
        }

        if (info instanceof HttpServletRequest) {
            sb.append(logReqResArgs.titleRequest());
            sb.append(JSON.toJSONString(getRequestArgs((HttpServletRequest) info)));
        } else {
            if (logReqResArgs.logResponseWithRequest()) {
                HttpServletRequest request = getHttpServletRequest();
                if (request != null) {
                    sb.append(logReqResArgs.titleRequest());
                    sb.append(JSON.toJSONString(getRequestArgs(request)));
                    sb.append("\n");
                }
            }
            sb.append(logReqResArgs.titleResponse());
            sb.append(JSON.toJSONString(info));
        }

        LoggerUtils.info(targetMethod.getDeclaringClass(), sb.toString());
    }


    /**
     * 获取默认日志头
     *
     * @param targetMethod
     * @return
     */
    private String getDefaultTitle(Method targetMethod) {

        Annotation[] annotations = targetMethod.getAnnotations();

        for (Annotation annotation : annotations) {
            if ("org.springframework.web.bind.annotation.RequestMapping".equals(annotation.annotationType().getName())) {
                try {
                    return (String) annotation.annotationType().getMethod("value").invoke(annotation);
                } catch (Exception ignored) {
                }
            }
        }

        return targetMethod.getName();
    }

    /**
     * 获取注解所在方法
     *
     * @param joinPoint
     * @return
     */
    private Method getMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }


    /**
     * 获取参数JSON字符串
     * 不支持raw类型
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private JSONObject getRequestArgs(HttpServletRequest request) {
        try {
            JSONObject jsonArgs = new JSONObject();
            Enumeration<String> args = request.getParameterNames();

            while (args.hasMoreElements()) {
                String key = args.nextElement();
                jsonArgs.put(key, request.getParameter(key));
            }

            return jsonArgs;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取HttpServletRequest
     *
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;
    }
}