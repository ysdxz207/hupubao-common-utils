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

import com.hupubao.common.error.ErrorInfo;
import com.hupubao.common.error.SystemError;
import com.hupubao.common.error.Throws;

/**
 * @author ysdxz207
 * @date 2019-06-27
 * <p>断言工具</p>
 * <p>注意某些场合下使用将会导致预期结果不正确</p>
 * <p>因此如：isTure方法仅用于单条件场合断言</p>
 * <p>不适用于[如果...且...]这种有前置条件的表达式语句</p>
 */
public class Assert {

    /**
     * <p>表达式是否为真</p>
     * <p>为假则抛出错误</p>
     *
     * @param expression
     * @param errorInfo
     * @param message
     */
    public static void isTure(boolean expression,
                              ErrorInfo errorInfo,
                              String message) {
        if (!expression) {
            Throws.throwError(errorInfo, message);
        }
    }

    public static void isTure(boolean expression,
                              ErrorInfo errorInfo) {
        isTure(expression, errorInfo, errorInfo.getErrorMsg());
    }

    /**
     * <p>表达式是否为假</p>
     * <p>为真则抛出错误</p>
     * @param expression
     * @param errorInfo
     * @param message
     */
    public static void isFalse(boolean expression,
                               ErrorInfo errorInfo,
                               String message) {
        isTure(!expression, errorInfo, message);
    }

    public static void isFalse(boolean expression,
                               ErrorInfo errorInfo) {
        isFalse(expression, errorInfo, errorInfo.getErrorMsg());
    }

    public static void notNull(Object object,
                               ErrorInfo errorInfo,
                               String message) {

        isTure(object != null, errorInfo, message);
    }

    public static void notNull(Object object,
                               ErrorInfo errorInfo) {
        notNull(object, errorInfo, errorInfo.getErrorMsg());
    }

    public static void notNull(Object object,
                               String message) {
        notNull(object, SystemError.SYSTEM_BISINESS_ERROR, message);
    }


}