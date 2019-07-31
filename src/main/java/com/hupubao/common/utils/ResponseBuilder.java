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

import com.hupubao.common.bean.ResponseBean;

/**
 * @author ysdxz207
 * @date 2019-07-04
 * 返回结构封装器
 */
public class ResponseBuilder {

    public static  <T> ResponseBean<T> buildOk() {
        return new ResponseBean<T>().success();
    }

    public static  <T> ResponseBean<T> buildOk(T data, String msg) {
        return new ResponseBean<T>().success(data, msg);
    }

    public static  <T> ResponseBean<T> buildOk(T data) {
        return buildOk(data, ResponseBean.MSG_SUCCESS);
    }
}