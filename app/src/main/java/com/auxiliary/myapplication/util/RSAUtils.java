package com.auxiliary.myapplication.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

public class RSAUtils {
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String COMMON_RSA = "RSA/ECB/PKCS1Padding";

    public static final String SERVER_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGvJuiVfqKjdSgDqpdBMkvbaMp\r\n" +
            "FP2cnD9cvj+terlIY8XLO6AHVIpI1u67vgpLUtb/mJIc3c7rf4nINKjukU2KcrMD\r\n" +
            "mWmvAkv/0NtHet1DwBUXB5eePh6gyt73GJDoGnE7uPnLYtvF/mdQRi3XhuSVAsH7\r\n" +
            "3Ko2em7fMwHJYxOQBwIDAQAB";
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL4mCQ8LIrA655sb\n" +
            "YAzBe0eHEXLFUONJBy5lrBfyR6LmUtM7G3dXhXFNIk2nca8bzq20UwVRJPnB8wSp\n" +
            "ND49izEgWg1fNDJdv517pHZxjtvw+sCqPB4qE7E3lD6EuUTF+EhaAkZeCJ6IzYPo\n" +
            "P4kvbH+tzVHHEIcXmmHoz9QAvhhvAgMBAAECgYEAhwrcRCLUb/RlqHK4tFZ1B0eV\n" +
            "zLz1xXWH9BuhLSCUX8zT5dCEoS03SFBPnHbe9k35asQ2cgA/EmH8BcNlpw5uWyt/\n" +
            "wCXpWQM2mZeKPJ996652kEB5ZJPGqRM+F07M+sOrt81vcT+n35Hw7Hl2i6ruHmtK\n" +
            "kSN4FH93zCnGzr5W6oECQQDqD3PjvRSxaomVOecnLQk/x6YXjELH1eQfKqqiTHfm\n" +
            "gbYX6+NoT1fHL7tRSBi8JzyplZwi+RfQ88fMzPG8UUvlAkEAz/jecf8skIaNh7RI\n" +
            "e5sNJVnVAxn1414dkDuVC6y5xdGXT90xJ79DzFmXEzW3jGKw99kJyExHzynWjwO3\n" +
            "jsGVwwJAHzmWhVBJPW3wEtdZrhFFBZw13ThaBFzVhQ7lGqfG6xps134hpV3IYQtO\n" +
            "GwbaPeeiISGTZdsDQV30Tq8cpLnXvQJAITzbAEkR7D80211awDZ1kWScAJTjkWT3\n" +
            "QJflKCqAvjbTAfaN5pZQ1ZXz6SQKo6saMWJEh/h4+YjsO4sSiQQzVQJBAIJjgF4t\n" +
            "KIW7HpzrNk4KqWGb98r4V6MLVhQcOc5YjtvISBI4PT0TXBQ9Jd5jhhEmQI+ZxUdg\n" +
            "QIiUDC6lpjyAyRg=";
    public static Map<String,String> createKeys(int keySize){
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);

        }catch (NoSuchAlgorithmException e){
            throw  new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }
        // 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyString =Base64Utils.encode(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyString = Base64Utils.encode(privateKey.getEncoded());
        Map<String,String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey",publicKeyString);
        keyPairMap.put("privateKey",privateKeyString);
        return  keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     */

    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
        // 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decode(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
        return  key;

    }

    /**
     * 得到私钥
     *@param privateKey  密钥字符串（经过base64编码）
     */
    public static RSAPrivateKey getPrivateKey(String privateKey)throws NoSuchAlgorithmException,InvalidKeySpecException{
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        // 通过PKCS#8编码的Key指令获得私钥对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKey));
        RSAPrivateKey key =  (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return  key;


    }

    /**
     * 公钥加密
     * @param data 要加密的数据
     * @param publicKey 公钥
     * @return
     */
    public static String publicEncrypt(String data,RSAPublicKey publicKey){
        try {
            Cipher cipher = Cipher.getInstance(COMMON_RSA);
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            return  Base64Utils.encode(rsaSplitCodec(cipher,Cipher.ENCRYPT_MODE,data.getBytes(CHARSET),
                    publicKey.getModulus().bitLength()));
        }catch (Exception e){
            throw  new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data 要解密的数据
     * @param privateKey 私钥
     * @return
     */
    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(COMMON_RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64Utils.decode(data),
                    privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data 要加密的数据
     * @param privateKey 私钥
     * @return
     */
    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(COMMON_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64Utils.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET),
                    privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }
    /**
     * 公钥解密
     * @param data 要解密的数据
     * @param publicKey 公钥
     * @return
     */
    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(COMMON_RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64Utils.decode(data),
                    publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }


    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.close(out);
        return resultDatas;
    }
}
