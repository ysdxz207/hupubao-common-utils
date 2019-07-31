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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

/**
 * @author ysdxz207
 * @date 2018-05-24
 * MD5工具
 */
public final class Md5Utils {
    private final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F'};

    public static String md5(byte[] bytes) {

        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(bytes);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                chars[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                chars[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5(String str) {
        byte[] btInput = (str).getBytes(StandardCharsets.UTF_8);
        return md5(btInput);
    }

    /**
     * <h1>md5签名</h1>
     *
     * @param key    私钥
     * @param params 参数
     * @return 签名字符串
     */
    public static String sign(String key,
                              Map<String, String> params) {

        String signPre = StringUtils.createLinkString(params) +
                "&key=" + key;
        return md5(signPre);
    }

    /**
     * <h2>验签</h2>
     *
     * @param key
     * @param sign
     * @param params
     * @return
     */
    public static boolean verify(String key,
                                 String sign,
                                 Map<String, String> params) {
        if (StringUtils.isBlank(sign)) {
            return false;
        }
        return sign.equals(sign(key, params));
    }

}
