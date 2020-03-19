package com.hupubao.common.logger.annotations;

import java.lang.annotation.*;

/**
 * @author ysdxz207
 * @date 2018-06-08
 * 是否记录请求及返回参数日志功能注解
 * 有此注解则记录日志
 * 使用条件：有HttpServletRequest和HttpServletResponse参数，且有返回值
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited// 支持继承，这样在aop的cglib动态代理类中才能获取到注解
public @interface LogReqResArgs {
    /**
     * 请求参数日志头，默认值为RequestMapping的路径，若无注解则是方法名
     *
     * @return
     */
    public String title() default "";

    public String titleRequest() default "request parameters:";

    public String titleRequestBody() default "request body:";

    public String titleResponse() default "response result:";

    public String titleException() default "exception:";

    public boolean logException() default true;

    /**
     * 记录返回结果时带上请求参数
     *
     * @return
     */
    public boolean logResponseWithRequest() default false;

    /**
     * 记录HTTP请求类型,GET,POST...
     * @return
     */
    public boolean logMethod() default true;

    /**
     * 记录请求链接
     * @return
     */
    public boolean logUrl() default true;

}
