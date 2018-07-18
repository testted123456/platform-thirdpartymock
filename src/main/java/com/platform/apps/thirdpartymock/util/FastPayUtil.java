package com.platform.apps.thirdpartymock.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.Signature;

import org.apache.commons.codec.binary.Base64;

public class FastPayUtil {
	
	public static String  localPublicKey=  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1NyXKwaNA180nCE+6Zrq\r\n" + 
			"9RoCFDY+oyoVy7aQsVFUbYtkP6UTSjDyvtGx5uRLjUMIFNDSz2a6J/lzg4uiu7r4\r\n" + 
			"O8yK8ItRVckWGnjZuFUQmVpeTpgaEmL1uYfaGl2ZT7ebPef9cCI1lhxmcPA9yBYB\r\n" + 
			"Ip7xFtg5DsXGU3FhR15NK0BcpjgmszAABV/eydndUeE8OJ9YKgbEeuyIP9zAuLDC\r\n" + 
			"rwgQ6mszrkOm50frB4Rr1cht5N6NYiSusvP8N0f6TZmz0Cub9P7nSLkerinmqiwW\r\n" + 
			"3wsnYUkR4aoqhF0NpRSVUpGjxEkEq3M5jYOHrEQQegj+s8R1w3Orf2soFn7JYd/y\r\n" + 
			"QwIDAQAB";
	
	public static String localPrivateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDU3JcrBo0DXzSc\r\n" + 
			"IT7pmur1GgIUNj6jKhXLtpCxUVRti2Q/pRNKMPK+0bHm5EuNQwgU0NLPZron+XOD\r\n" + 
			"i6K7uvg7zIrwi1FVyRYaeNm4VRCZWl5OmBoSYvW5h9oaXZlPt5s95/1wIjWWHGZw\r\n" + 
			"8D3IFgEinvEW2DkOxcZTcWFHXk0rQFymOCazMAAFX97J2d1R4Tw4n1gqBsR67Ig/\r\n" + 
			"3MC4sMKvCBDqazOuQ6bnR+sHhGvVyG3k3o1iJK6y8/w3R/pNmbPQK5v0/udIuR6u\r\n" + 
			"KeaqLBbfCydhSRHhqiqEXQ2lFJVSkaPESQSrczmNg4esRBB6CP6zxHXDc6t/aygW\r\n" + 
			"fslh3/JDAgMBAAECggEBAILqZOLmdYX0YRQBcoMUb5ttcofh0OZjNOKElzsnTSjO\r\n" + 
			"iUqHiXSg5DsuFx9tm3X3GxpKUM2UXbvGNSBbmiuh05hbtbw5wz1inoCLgURYtQvM\r\n" + 
			"Q6JM9AQI7x47io2asXcQ8p7BduMndxOqxeqCJ01PE0WqwZaOR5FpXA/n9K+DNSz6\r\n" + 
			"mpebPT8C6dpM8YFwNpK3uyqCvtCHXzNKIdjLsUpnOHFNxptQGertNgWWcDFnPqCB\r\n" + 
			"FHWGZB/koZcc1nkcbRsHHtDc3p8eHq/yDjDnCy+spaJCscpIz/cH92uxJ0UQc6j6\r\n" + 
			"IrbTvjkeLNuVqRfUPtxBHCh/9gF0CiIEl9Z67zBp/fECgYEA7A2DckkI2hSs3Aje\r\n" + 
			"zETeskMp9bN4r73+WE4y3pnAO+jw1J/f7R/IDLK0diPkFfMvX1dvSHwDAPoloFrg\r\n" + 
			"iM4MFEd5LJBKtESgi87pv9upEREHl172JZCqFqAChjMUMxZyhTFltQ0Mlvr29JYx\r\n" + 
			"FyiP3bBmNavufT2ASkXd7V1klGsCgYEA5tljW9uHNJdZ4oTPjDib/gbFyhhDWpdc\r\n" + 
			"UtG2n5QCTFoENS1eROeytMfBzZd/VYzVGHGi2zMXXHCvVEjDcj0MPIY9tw3962Ya\r\n" + 
			"lHDr5TGUaRNPdA6t/F31EW+tbQdxIgidO10/qg4d8FHW2SGPtumn3x8SUwJ3LcZB\r\n" + 
			"AMp01auez4kCgYBt9N2GHv9AluDunjMqc+pIn4CsMy3Er3wLJmILzxL81UVZxh9J\r\n" + 
			"FcTaJWRqo81bbCxk/RWZbir4CY39ZFezYke+Ko0HdS9XSaB/f6Li/C+FA6nFmTaP\r\n" + 
			"yRNx9pc2bBS8t3ghEyGNdMaojb77r/quDvb1DXaPCcfODxfzgRgfuaFggQKBgQDD\r\n" + 
			"asb3XZDI7k6CuJbRRqKWmbkZaQyrRXI8EZcc4zhG1fS151/jyJe4/ViL53E9RGin\r\n" + 
			"tpp8IHXoJOPm32nD7PXMzn6z1T5b5SUe/7BRZnURte0Oe9bAyfDI/9a5cxFMFCq1\r\n" + 
			"tCxOkoC5jS3oxMZZutYGxNDIQFAPCcLBHqFkt1FKsQKBgQDQlLXTCx+nP7J8P8IF\r\n" + 
			"VSzgfkRJoHyE0dI5wVKmeSIyspBFOWN3ONNhARbx0Et4lk8C3mjnvipqV2bpJ3DA\r\n" + 
			"jr8xS6lfLXDjhNdc/qm9wDqqOtGUxC2ZaT9yROviL9jPvdU7zl98DUe2zOMZ5lGU\r\n" + 
			"miMf91dHF++BxD1gljaaD0aJPA==";
	
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
