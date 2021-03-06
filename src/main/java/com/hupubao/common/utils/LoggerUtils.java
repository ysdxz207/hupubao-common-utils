/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hupubao.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ysdxz207
 * @date 2018-05-24
 * 日志记录工具
 */
public class LoggerUtils {

    /**
     * <p>获取日志记录对象，若有调用方则日志打印变量[%c]为调用类</p>
     * <p>否则为LoggerUtils.class</p>
     *
     * @return
     */
    private static Logger getLogger() {
        String clazzCallerName = new Exception().getStackTrace()[2].getClassName();
        Class clazzCaller = null;
        try {
            clazzCaller = Class.forName(clazzCallerName);
        } catch (ClassNotFoundException ignored) {
        }


        if (clazzCaller == null) {
            return LoggerFactory.getLogger(LoggerUtils.class);
        }

        return getLogger(clazzCaller);
    }

    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void warn(Throwable t) {
        getLogger().warn(t != null ? t.getMessage() : "", t);
    }

    public static void warn(String message) {
        getLogger().warn(message);
    }

    /**
     * @param format 占位符为{}，如：LoggerUtils.warn("参数是{}", "name")
     * @param args   占位符对应的参数
     */
    public static void warn(String format,
                            Object... args) {
        getLogger().warn(format, args);
    }

    public static void warn(String message,
                            Throwable t) {
        getLogger().warn(message, t);
    }

    public static void warn(Class clazz,
                            String message) {
        getLogger(clazz).warn(message);
    }

    public static void warn(Class clazz,
                            String message,
                            Throwable t) {
        getLogger(clazz).warn(message, t);
    }

    public static void debug(Throwable t) {
        getLogger().debug(t != null ? t.getMessage() : "", t);
    }

    public static void debug(String message) {
        getLogger().debug(message);
    }

    /**
     * @param format 占位符为{}，如：LoggerUtils.debug("参数是{}", "name")
     * @param args   占位符对应的参数
     */
    public static void debug(String format,
                             Object... args) {
        getLogger().debug(format, args);
    }

    public static void debug(String message,
                             Throwable t) {
        getLogger().debug(message, t);
    }

    public static void debug(Class clazz,
                             String message) {
        getLogger(clazz).debug(message);
    }

    public static void debug(Class clazz,
                             String message,
                             Throwable t) {
        getLogger(clazz).debug(message, t);
    }

    public static void info(Throwable t) {
        getLogger().info(t != null ? t.getMessage() : "", t);
    }


    public static void info(String message) {
        getLogger().info(message);
    }

    /**
     * @param format 占位符为{}，如：LoggerUtils.info("参数是{}", "name")
     * @param args   占位符对应的参数
     */
    public static void info(String format,
                            Object... args) {
        getLogger().info(format, args);
    }

    public static void info(String message,
                            Throwable t) {
        getLogger().info(message, t);
    }

    public static void info(Class clazz,
                            String message) {
        getLogger(clazz).info(message);
    }

    public static void info(Class clazz,
                            String message,
                            Throwable t) {
        getLogger(clazz).info(message, t);
    }

    public static void error(Throwable t) {
        getLogger().error(t != null ? t.getMessage() : "", t);
    }

    public static void error(String message) {
        getLogger().error(message);
    }

    /**
     * @param format 占位符为{}，如：LoggerUtils.error("参数是{}", "name")
     * @param args   占位符对应的参数
     */
    public static void error(String format,
                             Object... args) {
        getLogger().error(format, args);
    }

    public static void error(String message,
                             Throwable t) {
        getLogger().error(message, t);
    }

    public static void error(Class clazz,
                             String message) {
        getLogger(clazz).error(message);
    }

    public static void error(Class clazz,
                             String message,
                             Throwable t) {
        getLogger(clazz).error(message, t);
    }

}
