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
package com.seichiiwei.common.bean;

import com.alibaba.fastjson.JSON;
import com.seichiiwei.common.error.ErrorInfo;
import com.seichiiwei.common.exceptions.BusinessException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ysdxz207
 * @date 2019-06-06
 * 接口响应结果
 */
@Data
@Accessors(chain = true)
public class ResponseBean<T> {

    private static final long serialVersionUID = 1L;

    public static final String CODE_SUCCESS = "SUCCESS";
    public static final String MSG_SUCCESS = "Success.";
    public static final String CODE_FAIL = "ERROR";
    public static final String MSG_FAIL = "Business Error.";

    private String code = CODE_FAIL;
    private String msg = MSG_FAIL;
    private T data;
    private Long timestamp;


    public ResponseBean<T> error(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public ResponseBean<T> success(T data, String msg) {
        this.code = CODE_SUCCESS;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
        this.data = data;
        return this;
    }

    public ResponseBean<T> success(String msg) {
        return success(null, msg);
    }

    public ResponseBean<T> success() {
        success(MSG_SUCCESS);
        return this;
    }

    public ResponseBean<T> error(Throwable e, String defaultMessage) {
        BusinessException businessException = this.getBusinessException(e, defaultMessage);
        error(businessException.getCode(), businessException.getMessage());
        return this;
    }


    public ResponseBean<T> error(Throwable e) {
        return error(e, MSG_FAIL);
    }

    public ResponseBean<T> error(ErrorInfo error) {
        error(error.getErrorCode(), error.getErrorMsg());
        return this;
    }

    public ResponseBean<T> error(ErrorInfo error, String msg) {
        error(error.getErrorCode(), msg);
        return this;
    }

    private BusinessException getBusinessException(Throwable e, String defaultMessage) {
        if (e == null) {
            return new BusinessException("FAIL", StringUtils.isEmpty(defaultMessage) ? "Fail." : defaultMessage);
        } else {
            return e instanceof BusinessException && e.getCause() == null ? (BusinessException)e : this.getBusinessException(e.getCause(), defaultMessage);
        }
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
