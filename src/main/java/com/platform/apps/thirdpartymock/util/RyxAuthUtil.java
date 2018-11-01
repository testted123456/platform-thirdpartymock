package com.platform.apps.thirdpartymock.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import com.alibaba.fastjson.JSONObject;

public class RyxAuthUtil {
	
	/**
     * 
     * 【方法名】 : 返回数据解密 <br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:28:59 .<br/>
     * 【参数】： .<br/>
     * 
     * @param input
     *            输入流
     * @param enCoding
     *            编码格式
     * @param privateKey
     *            私钥
     * @param publicKey
     *            公钥
     * @return 返回结果
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: madman 修改描述：创建新新件 .<br/>
     *         <p/>
     */
     public static String decode(InputStream input, String enCoding, String privateKey, String publicKey) throws Exception {
        byte[] dataLenBytes = new byte[8];
        input.read(dataLenBytes);
        int dataLen = Integer.parseInt(new String(dataLenBytes, enCoding));
        // LogCvt.info("报文总长度：" + dataLen);

        // 8位合作方编号
        byte[] systemIdBytes = new byte[8];
        input.read(systemIdBytes);
        // String systemId = new String(systemIdBytes, enCoding);
        // LogCvt.info("合作方编号：" + systemId);

        // 4位签名域长度
        byte[] signLenBytes = new byte[4];
        input.read(signLenBytes);
        int signLen = Integer.parseInt(new String(signLenBytes, enCoding));
        // LogCvt.info("签名域长度：" + signLen);

        // 签名域值
        byte[] signBytes = new byte[signLen];
        input.read(signBytes);
        // 报文数据主体密文
        byte[] dataBytes = new byte[dataLen - 8 - 4 - signLen];
        input.read(dataBytes);

        SignatureUtilQuickPay signUtil = new SignatureUtilQuickPay();
        signUtil.initKey(privateKey, publicKey, 2048);

        byte[] plainBytes = signUtil.decryptRSA(dataBytes, false, Charset.forName(enCoding));
        String plainText = new String(plainBytes, enCoding);
//        LogPay.debug("解析到明文：" + plainText);
        if (!signUtil.verifyRSA(plainBytes, signBytes, false, Charset.forName(enCoding))) {
            throw new UnsupportedOperationException("验签失败");
        }
        JSONObject json = JSONObject.parseObject(plainText);
        return json.toString();
    }
     
     public static String decode(byte[] bytes, String enCoding, String privateKey, String publicKey) throws Exception {
         byte[] dataLenBytes = new byte[8];
         System.arraycopy(bytes, 0, dataLenBytes, 0, 8);
//         input.read(dataLenBytes);
         int dataLen = Integer.parseInt(new String(dataLenBytes, enCoding));
         // LogCvt.info("报文总长度：" + dataLen);

         // 8位合作方编号
         byte[] systemIdBytes = new byte[8];
//         input.read(systemIdBytes);
         System.arraycopy(bytes, 8, systemIdBytes, 0, 8);
         // String systemId = new String(systemIdBytes, enCoding);
         // LogCvt.info("合作方编号：" + systemId);

         // 4位签名域长度
         byte[] signLenBytes = new byte[4];
//         input.read(signLenBytes);
         System.arraycopy(bytes, 16, signLenBytes, 0, 4);
         int signLen = Integer.parseInt(new String(signLenBytes, enCoding));
         // LogCvt.info("签名域长度：" + signLen);

         // 签名域值
         byte[] signBytes = new byte[signLen];
//         input.read(signBytes);
         
         // 报文数据主体密文
         byte[] dataBytes = new byte[dataLen - 8 - 4 - signLen];
//         input.read(dataBytes);
         System.arraycopy(bytes, 8+8+4+signLen, dataBytes, 0, dataLen - 8 - 4 - signLen);

         SignatureUtilQuickPay signUtil = new SignatureUtilQuickPay();
         signUtil.initKey(privateKey, publicKey, 2048);

         byte[] plainBytes = signUtil.decryptRSA(dataBytes, false, Charset.forName(enCoding));
         String plainText = new String(plainBytes, enCoding);
//         LogPay.debug("解析到明文：" + plainText);
         if (!signUtil.verifyRSA(plainBytes, signBytes, false, Charset.forName(enCoding))) {
             throw new UnsupportedOperationException("验签失败");
         }
         JSONObject json = JSONObject.parseObject(plainText);
         return json.toString();
     }


    /**
     * 
     * 【方法名】 : 请求数据加密 <br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:29:36 .<br/>
     * 【参数】： .<br/>
     * 
     * @param params
     *            请求参数
     * @param systemId
     *            请求机构号
     * @param enCoding
     *            编码格式
     * @param privateKey
     *            私钥
     * @param publicKey
     *            公钥
     * @return 返回结果
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: madman 修改描述：创建新新件 .<br/>
     *         <p/>
     */
     public static byte[] encode(String params, String systemId, String enCoding, String privateKey, String publicKey) throws Exception {
        SignatureUtilQuickPay signUtil = new SignatureUtilQuickPay();
        signUtil.initKey(privateKey, publicKey, 2048);
        byte[] postData = params.getBytes(enCoding);
        byte[] signData = signUtil.signRSA(postData, false, enCoding);
        byte[] encryptData = signUtil.encryptRSA(postData, false, enCoding);
        String dataLen = lpad(String.valueOf(8 + 4 + signData.length + encryptData.length), "0", 8);
        String signDataLen = lpad(signData.length + "", "0", 4);

        // 8位报文总长度、8位合作方编号、4位签名域长度、签名域值、报文数据主体密文
        int total = 8 + 8 + 4 + signData.length + encryptData.length;
        byte[] data = new byte[total];

        System.arraycopy(dataLen.getBytes(enCoding), 0, data, 0, 8);
        System.arraycopy(systemId.getBytes(enCoding), 0, data, 8, 8);
        System.arraycopy(signDataLen.getBytes(enCoding), 0, data, 16, 4);
        System.arraycopy(signData, 0, data, 20, signData.length);
        System.arraycopy(encryptData, 0, data, 20 + signData.length, encryptData.length);
        return data;
    }

    /**
     * 
     * 【方法名】 : 左补0<br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:32:49 .<br/>
     * 【参数】： .<br/>
     * 
     * @param src
     *            源字符串
     * @param r
     *            左补的字符
     * @param length
     *            补的长度
     * @return .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: madman 修改描述：创建新新件 .<br/>
     *         <p/>
     */
     public static String lpad(String src, String r, int length) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buf.append(r);
        }
        String tmp = buf.toString() + src;
        return tmp.substring(tmp.length() - length);
    }

}
