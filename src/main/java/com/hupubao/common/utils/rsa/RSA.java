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

package com.hupubao.common.utils.rsa;

import com.hupubao.common.utils.Md5Utils;
import com.hupubao.common.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;


/**
 * @author ysdxz207
 * @date 2017年1月13日
 * RSA密钥生成工具
 * 因本工具类使用随机数，在tomcat下使用时可能需加上启动参数：
 * -Djava.security.egd=file:/dev/./urandom
 */
public class RSA {


    private static final String ALGORITHMS = "RSA";

    private static String CHARSET = "UTF-8";

    /**
     * 密钥长度
     */
    private static int KEYSIZE = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static int MAX_DECRYPT_BLOCK = 128;

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    private RSA() {
    }

    public static RSA getInstance() {
        RSA instance = RSAKeyUtilsInstance.INSTANCE.singleton;
        recomputeBlock();
        return instance;
    }

    private enum RSAKeyUtilsInstance {
        INSTANCE;

        RSAKeyUtilsInstance() {
            singleton = new RSA();
        }

        private RSA singleton;
    }

    private static void recomputeBlock() {

        //重新计算分段大小
        MAX_ENCRYPT_BLOCK = KEYSIZE / 8 - 11;
        MAX_DECRYPT_BLOCK = KEYSIZE / 8;
    }

    /////////////生成工具

    public static class RSAKey {
        private String privateKey;
        private String publicKey;

        public RSAKey(String privateKey, String publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }
    }


    public RSA keySize(int keySize) {
        KEYSIZE = keySize;
        recomputeBlock();
        return this;
    }

    public RSA rsaKey(RSAKey rsaKey) {
        try {
            byte[] privateKeyBytes = Base64.decodeBase64(rsaKey.getPrivateKey());
            byte[] publicKeyBytes = Base64.decodeBase64(rsaKey.getPublicKey());
            PKCS8EncodedKeySpec pkcs8KeySpecPrivate = new PKCS8EncodedKeySpec(privateKeyBytes);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
            privateKey = keyFactory.generatePrivate(pkcs8KeySpecPrivate);
            publicKey = keyFactory.generatePublic(x509KeySpec);

            RSAPrivateKeySpec keySpecPrivate = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
            RSAPublicKeySpec keySpecPublic = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            int keySizePrivate = keySpecPrivate.getModulus().toString(2).length();
            int keySizePublic = keySpecPublic.getModulus().toString(2).length();

            if (keySizePrivate != keySizePublic) {
                throw new RuntimeException("Public and private keys are not a pair.");
            }

            KEYSIZE = keySizePrivate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        recomputeBlock();
        return this;
    }

    public RSA charset(String charset) {
        CHARSET = charset;
        return this;
    }

    public RSAKey generateRSAKey() throws NoSuchAlgorithmException {

        //RSA算法要求有一个可信任的随机数源
        SecureRandom secureRandom = new SecureRandom();

        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHMS);

        //利用上面的随机数据源初始化这个KeyPairGenerator对象
        keyPairGenerator.initialize(KEYSIZE, secureRandom);

        //生成密匙对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //得到公钥
        Key publicKey = keyPair.getPublic();

        //得到私钥
        Key privateKey = keyPair.getPrivate();

        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();

        String publicKeyBase64 = Base64.encodeBase64String(publicKeyBytes);
        String privateKeyBase64 = Base64.encodeBase64String(privateKeyBytes);

        //java语言需要pkcs8
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
            PrivateKey privateKey2 = keyFactory.generatePrivate(keySpec);
            privateKeyBase64 = Base64.encodeBase64String(privateKey2.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RSAKey(privateKeyBase64, publicKeyBase64);
    }

    //////////////////签名，验签

    public static enum SignType {
        RSA("SHA1WithRSA"),
        RSA2("SHA256WithRSA");

        SignType(String algorithm) {
            this.algorithm = algorithm;
        }

        public String algorithm;
    }

    public String sign(String privateKey,
                       Map<String, String> params,
                       SignType signType) {

        privateKey = StringUtils.replaceBlank(privateKey);
        String prestr = StringUtils.createLinkString(params);
        String md = Md5Utils.md5(getContentBytes(prestr));
        String mysign = buildSign(md, privateKey, signType);
        return mysign;
    }

    private String buildSign(String content,
                             String privateKey,
                             SignType signType) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance(signType.algorithm);

            signature.initSign(priKey);
            signature.update(content.getBytes(CHARSET));

            byte[] signed = signature.sign();

            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getContentBytes(String content) {
        if (CHARSET == null || "".equals(CHARSET)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + CHARSET);
        }
    }

    /**
     * @param sPara 签名参数
     * @param sign  签名字符串
     * @return
     */
    public boolean verify(Map<String, String> sPara,
                          String sign,
                          SignType signType) {

        if (publicKey == null) {
            throw new NoPublicKeyException("Public key is null.Please call `rsaKey(RSAKey rsaKey)` method first.");
        }
        String prestr = StringUtils.createLinkString(sPara);
        String md = Md5Utils.md5(getContentBytes(prestr));


        try {

            Signature signature = Signature.getInstance(signType.algorithm);

            signature.initVerify(publicKey);
            signature.update(md.getBytes(CHARSET));

            return signature.verify(Base64.decodeBase64(sign));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    //////////////////加密解密

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @return
     * @throws Exception
     */
    public String decryptByPrivateKey(String encryptedData)
            throws Exception {
        return decrypt(privateKey, encryptedData);
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @return
     * @throws Exception
     */
    public String decryptByPublicKey(String encryptedData)
            throws Exception {
        return decrypt(publicKey, encryptedData);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @return
     * @throws Exception
     */
    public String encryptByPublicKey(String data)
            throws Exception {
        return encrypt(publicKey, data);
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @return
     * @throws Exception
     */
    public String encryptByPrivateKey(String data)
            throws Exception {
        return encrypt(privateKey, data);
    }

    private String encrypt(Key key,
                           String data) throws Exception {
        byte[] dataBytes = data.getBytes(CHARSET);
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 对数据分段加密
        byBlock(Cipher.ENCRYPT_MODE, out, cipher, dataBytes, inputLen);
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encodeBase64String(encryptedData);
    }

    private String decrypt(Key key,
                           String encryptedData) throws Exception {
        byte[] encryptedDataBytes = Base64.decodeBase64(encryptedData);
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.DECRYPT_MODE, key);
        int inputLen = encryptedDataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 对数据分段解密
        byBlock(Cipher.DECRYPT_MODE, out, cipher, encryptedDataBytes, inputLen);

        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, CHARSET);
    }

    private void byBlock(int mode,
                         ByteArrayOutputStream out,
                         Cipher cipher,
                         byte[] data,
                         int inputLen) throws Exception {

        int maxBlockLength = (mode == Cipher.ENCRYPT_MODE ? MAX_ENCRYPT_BLOCK : MAX_DECRYPT_BLOCK);
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlockLength) {
                cache = cipher.doFinal(data, offSet, maxBlockLength);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlockLength;
        }
    }

    class NoPublicKeyException extends RuntimeException {
        private static final long serialVersionUID = 4511932659620947111L;

        public NoPublicKeyException() {
        }

        public NoPublicKeyException(String message) {
            super(message);
        }
    }
}