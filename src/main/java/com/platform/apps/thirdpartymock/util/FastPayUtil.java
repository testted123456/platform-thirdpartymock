package com.platform.apps.thirdpartymock.util;

import java.io.InputStream;
import java.nio.charset.Charset;

public class FastPayUtil {
	
	 /**
     * 
     * 【方法名】 : 编码 <br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年7月11日 下午5:55:47 .<br/>
     * 【参数】： .<br/>
     * 
     * @param reqXml
     *            请求参数
     * @param code
     *            商户号
     * @param enCode
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
    public static byte[] encode(String reqXml, String code, String enCode, String privateKey, String publicKey) throws Exception {
        SignatureUtilQuickPay signUtil = new SignatureUtilQuickPay();
        signUtil.initKey(privateKey, publicKey, 2048);
        byte[] postData = reqXml.getBytes(enCode);
        byte[] signData = signUtil.signRSA(postData, false, enCode);
        byte[] encryptData = signUtil.encryptRSA(postData, false, enCode);
        String signDataLen = lpad(signData.length + "", "0", 4);
        String dataLen = lpad(String.valueOf(4 + signDataLen.getBytes(enCode).length + signData.length + encryptData.length), "0", 8);
        byte[] data = new byte[Integer.valueOf(dataLen) + 8];
        System.arraycopy(dataLen.getBytes(enCode), 0, data, 0, 8);
        System.arraycopy(code.getBytes(enCode), 0, data, 8, 4);
        System.arraycopy(signDataLen.getBytes(enCode), 0, data, 12, 4);
        System.arraycopy(signData, 0, data, 16, signData.length);
        System.arraycopy(encryptData, 0, data, 16 + signData.length, encryptData.length);
        return data;
    }
    
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
        //报文总长度
    	byte[] dataLenBytes = new byte[8];
        input.read(dataLenBytes);
        int dataLen = Integer.parseInt(new String(dataLenBytes, enCoding));
        
        //合作方编码
        byte[] systemIdBytes = new byte[4];
        input.read(systemIdBytes);
        String vv = new String(systemIdBytes, enCoding);
        
        //签名域长度
        byte[] signLenBytes = new byte[4];
        input.read(signLenBytes);
        int signLen = Integer.parseInt(new String(signLenBytes, enCoding));
        
        //签名域值,不经Base64编码
        byte[] signBytes = new byte[signLen];
        input.read(signBytes);
        
        //XML报文主体密文,不经Base64编码
        byte[] dataBytes = new byte[dataLen - 4 - 4 - signLen];
        input.read(dataBytes);
        
        SignatureUtilQuickPay signUtil = new SignatureUtilQuickPay();
        signUtil.initKey(privateKey, publicKey, 2048);
        
        byte[] plainBytes = signUtil.decryptRSA(dataBytes, false, Charset.forName(enCoding));
        
        String plainText = new String(plainBytes, enCoding);
        if (!signUtil.verifyRSA(plainBytes, signBytes, false, Charset.forName(enCoding))) {
            throw new UnsupportedOperationException("验签失败");
        }
        return plainText.toString();
    }
    
    public static String decode(byte[] firstDomain, byte[] input, String enCoding, String privateKey, String publicKey) throws Exception {
        //报文总长度
//    	byte[] dataLenBytes = new byte[8];
    	byte[] dataLenBytes = firstDomain;
//        input.read(dataLenBytes);
//        System.arraycopy(input, 0, dataLenBytes, 0, 8);
        int dataLen = Integer.parseInt(new String(dataLenBytes, enCoding));
        
        //合作方编码
        byte[] systemIdBytes = new byte[4];
//        input.read(systemIdBytes);
        System.arraycopy(input, 0, systemIdBytes, 0, 4);
        String vv = new String(systemIdBytes, enCoding);
        
        //签名域长度
        byte[] signLenBytes = new byte[4];
//        input.read(signLenBytes);
        System.arraycopy(input, 4, signLenBytes, 0, 4);
        int signLen = Integer.parseInt(new String(signLenBytes, enCoding));
        
        //签名域值,不经Base64编码
        byte[] signBytes = new byte[signLen];
//        input.read(signBytes);
        System.arraycopy(input, 8, signBytes, 0, signLen);
        
        //XML报文主体密文,不经Base64编码
        byte[] dataBytes = new byte[dataLen  -8 - signLen ];
//        input.read(dataBytes);
        System.arraycopy(input, 8 + signLen, dataBytes, 0, dataLen - 8 - signLen);
        
        SignatureUtilQuickPay signUtil = new SignatureUtilQuickPay();
        signUtil.initKey(privateKey, publicKey, 2048);
        
        byte[] plainBytes = signUtil.decryptRSA(dataBytes, false, Charset.forName(enCoding));
        
        String plainText = new String(plainBytes, enCoding);
        if (!signUtil.verifyRSA(plainBytes, signBytes, false, Charset.forName(enCoding))) {
            throw new UnsupportedOperationException("验签失败");
        }
        return plainText.toString();
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
    private static String lpad(String src, String r, int length) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buf.append(r);
        }
        String tmp = buf.toString() + src;
        return tmp.substring(tmp.length() - length);
    }

}
