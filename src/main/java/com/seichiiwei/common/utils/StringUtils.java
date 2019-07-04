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

package com.seichiiwei.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author ysdxz207
 * @date 2017-08-10
 */
public class StringUtils {

    public static boolean isBlank(Object obj) {
        return obj == null || org.apache.commons.lang3.StringUtils.isBlank(obj.toString())
                || obj.toString().equalsIgnoreCase("null");
    }

    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().isEmpty();
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static Integer parseInteger(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Integer.valueOf(m.replaceAll("").trim());
    }

    /**
     * <h1>首字母转大写</h1>
     *
     * @param name
     * @return
     */
    public static String firstToUpperCase(String name) {
        return StringUtils.isBlank(name) ? "" : name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * <h1>首字母转小写</h1>
     *
     * @param name
     * @return
     */
    public static String firstToLowerCase(String name) {
        return StringUtils.isBlank(name) ? "" : name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String join(Object[] strAry, String join) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }

        return new String(sb);
    }

    /**
     * <h1>删除空格，换行符，制表符</h1>
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * <h1>将url参数解析为json</h1>
     *
     * @param urlParams
     * @param charset
     * @return
     */
    public static JSONObject parseUrlParameters(String urlParams, String charset) {
        JSONObject jsonObject = new JSONObject();
        if (isBlank(urlParams)) {
            return jsonObject;
        }
        String[] params = urlParams.split("&");
        for (String param : params) {
            String[] p = param.split("=");
            if (p.length == 2) {
                String value = p[1];
                try {
                    value = URLDecoder.decode(p[1], charset);
                } catch (UnsupportedEncodingException ignored) {
                }
                jsonObject.put(p[0], value);
            }
        }
        return jsonObject;
    }

    /**
     * <h1>检查是否是json字符串</h1>
     * @param str
     * @return
     */
    public static boolean isJsonString(String str) {
        boolean result = false;
        try {
            JSON.parse(str);
            result = true;
        } catch (Exception ignored) {
        }
        return result;
    }

    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder preStr = new StringBuilder();
        for (String key : keys) {
            String value = params.get(key);
            if (value == null || "".equals(value.trim())) {
                continue;
            }
            if (!"sign".equals(key)) {
                preStr.append(key).append("=").append(value).append("&");
            }
        }
        return preStr.substring(0, preStr.length() - 1);
    }

    /**
     * <h1>乱码检测</h1>
     * @param str
     * @return
     */
    public static boolean isGarbled(String str) {
        try {
            if (isEmpty(str)) {
                return false;
            }
            String after = replaceBlank(str);
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();

            for (char c : ch) {
                if (!(c >= 32 && c <= 126)) {
                    String string = "" + c;
                    if (!string.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LoggerUtils.error("[乱码检测异常][{}]", str, e);
        }

        return false;
    }
}
