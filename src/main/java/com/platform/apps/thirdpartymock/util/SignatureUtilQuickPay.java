package com.platform.apps.thirdpartymock.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.apps.thirdpartymock.component.MyCommandLineRunner;

import sun.misc.BASE64Decoder;


/**
 * 
 * <pre>
 * 【类型】: SignatureUtil <br/> 
 * 【作用】: RSA加密签名工具类
 * 【时间】：2017年5月18日 下午4:14:00 <br/> 
 * 【作者】：madman <br/>
 * </pre>
 */
@SuppressWarnings("all")
public class SignatureUtilQuickPay {
	
	public static Logger logger = LoggerFactory.getLogger(SignatureUtilQuickPay.class);

    /**
     * RSA签名方式
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    /**
     * RSA加密方式 加密block需要预留11字节
     */
    public static final String CIPHER_ALGORITHM    = "RSA/ECB/PKCS1Padding";
    /**
     * 密钥长度
     */
    public static final int    KEYBIT              = 2048;
    /**
     * 字节数
     */
    public static final int    RESERVEBYTES        = 11;
    /**
     * 私钥
     */
    private PrivateKey         localPrivKey;
    /**
     * 公钥
     */
    private PublicKey          peerPubKey;

    /**
     * 初始化自己的私钥,对方的公钥以及密钥长度.
     *
     * @param localPrivKeyBase64Str
     *            Base64编码的私钥,PKCS#8编码. (去掉pem文件中的头尾标识)
     * @param peerPubKeyBase64Str
     *            Base64编码的公钥. (去掉pem文件中的头尾标识)
     * @param keysize
     *            密钥长度, 一般2048
     * @exception Exception
     *                抛出异常
     */
    public void initKey(String localPrivKeyBase64Str, String peerPubKeyBase64Str, int keysize) throws Exception {
        try {
            localPrivKey = getPrivateKey(localPrivKeyBase64Str);
            peerPubKey = getPublicKey(peerPubKeyBase64Str);
        } catch (InvalidKeySpecException e) {
        	logger.error("初始化秘钥异常:" + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in
     *            公钥输入流
     * @throws Exception
     *             加载公钥时产生的异常
     * @return 返回公钥
     */
    public RSAPublicKey getPublicKey(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return getPublicKey(sb.toString());
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                throw new Exception("关闭输入缓存流出错");
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                throw new Exception("关闭输入流出错");
            }
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载公钥时产生的异常
     * @return 获取公钥
     */

    public RSAPublicKey getPublicKey(String publicKeyStr) throws Exception {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("公钥非法");
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 
     * 【方法名】 : 从文件中加载私钥 <br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:18:31 .<br/>
     * 【参数】： .<br/>
     * 
     * @param in
     *            输入流
     * @return 返回私钥
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: Administrator 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public RSAPrivateKey getPrivateKey(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return getPrivateKey(sb.toString());
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("私钥输入流为空");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                throw new Exception("关闭输入缓存流出错");
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                throw new Exception("关闭输入流出错");
            }
        }
    }

    /**
     * 从字符串中加载私钥
     *
     * @param privateKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载私钥时产生的异常
     * @return 返回私钥
     */
    public RSAPrivateKey getPrivateKey(String privateKeyStr) throws Exception {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("私钥非法");
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
        	logger.error(e.getMessage(), e);
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 
     * 【方法名】 :RAS加密<br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:19:33 .<br/>
     * 【参数】： .<br/>
     * 
     * @param plainBytes
     *            加密字节
     * @param useBase64Code
     *            是否base64编码
     * @param charset
     *            编码格式
     * @return 返回字节
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: madman 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public byte[] encryptRSA(byte[] plainBytes, boolean useBase64Code, String charset) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        int decryptBlock = KEYBIT / 8; // 256 bytes
        int encryptBlock = decryptBlock - RESERVEBYTES; // 245 bytes
        // 计算分段加密的block数 (向上取整)
        int nBlock = plainBytes.length / encryptBlock;
        if ((plainBytes.length % encryptBlock) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }
        // 输出buffer, 大小为nBlock个decryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlock);
        cipher.init(Cipher.ENCRYPT_MODE, peerPubKey);
        // 分段加密
        for (int offset = 0; offset < plainBytes.length; offset += encryptBlock) {
            // block大小: encryptBlock 或 剩余字节数
            int inputLen = plainBytes.length - offset;
            if (inputLen > encryptBlock) {
                inputLen = encryptBlock;
            }
            // 得到分段加密结果
            byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
            // 追加结果到输出buffer中
            outbuf.write(encryptedBlock);
        }
        // 如果是Base64编码，则返回Base64编码后的数组
        if (useBase64Code) {
            return Base64.encodeBase64String(outbuf.toByteArray()).getBytes(charset);
        } else {
            return outbuf.toByteArray(); // ciphertext
        }
    }

    /**
     * 
     * 【方法名】 : RSA解密 <br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:20:57 .<br/>
     * 【参数】： .<br/>
     * 
     * @param cryptedBytes
     *            解密字节
     * @param useBase64Code
     *            是否base64编码
     * @param charset
     *            编码字符
     * @return 返回结果
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: madman 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public byte[] decryptRSA(byte[] cryptedBytes, boolean useBase64Code, Charset charset) throws Exception {
        byte[] data = null;
        // 如果是Base64编码的话，则要Base64解码
        if (useBase64Code) {
            data = Base64.decodeBase64(new String(cryptedBytes, charset));
        } else {
            data = cryptedBytes;
        }
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        int decryptBlock = KEYBIT / 8; // 256 bytes
        int encryptBlock = decryptBlock - RESERVEBYTES; // 245 bytes
        // 计算分段解密的block数 (理论上应该能整除)
        int nBlock = data.length / decryptBlock;
        // 输出buffer, , 大小为nBlock个encryptBlock
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * encryptBlock);
        cipher.init(Cipher.DECRYPT_MODE, localPrivKey);
        // 分段解密
        for (int offset = 0; offset < data.length; offset += decryptBlock) {
            // block大小: decryptBlock 或 剩余字节数
            int inputLen = data.length - offset;
            if (inputLen > decryptBlock) {
                inputLen = decryptBlock;
            }

            // 得到分段解密结果
            byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
            // 追加结果到输出buffer中
            outbuf.write(decryptedBlock);
        }
        outbuf.flush();
        outbuf.close();
        return outbuf.toByteArray();
    }

    /**
     * 
     * 【方法名】 : RSA签名 <br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:22:05 .<br/>
     * 【参数】： .<br/>
     * 
     * @param plainBytes
     *            签名字节
     * @param useBase64Code
     *            是否base64编码
     * @param charset
     *            加密字符
     * @return 返回结果
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: madman 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public byte[] signRSA(byte[] plainBytes, boolean useBase64Code, String charset) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(localPrivKey);
        signature.update(plainBytes);

        // 如果是Base64编码的话，需要对签名后的数组以Base64编码
        if (useBase64Code) {
            return Base64.encodeBase64String(signature.sign()).getBytes(charset);
        } else {
            return signature.sign();
        }
    }

    /**
     * 
     * 【方法名】 :验签操作<br/>
     * 【作者】: madman .<br/>
     * 【时间】： 2017年5月18日 下午4:22:51 .<br/>
     * 【参数】： .<br/>
     * 
     * @param plainBytes
     *            请求报文
     * @param signBytes
     *            签名内容
     * @param useBase64Code
     *            是否base64编码
     * 
     * @param charset
     *            编码格式
     * @return 返回结果
     * @throws Exception .<br/>
     *         <p>
     *         修改记录.<br/>
     *         修改人: Administrator 修改描述：创建新新件 .<br/>
     *         <p/>
     */
    public boolean verifyRSA(byte[] plainBytes, byte[] signBytes, boolean useBase64Code, Charset charset) throws Exception {
        boolean isValid = false;
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(peerPubKey);
        signature.update(plainBytes);
        // 如果是Base64编码的话，需要对验签的数组以Base64解码
        if (useBase64Code) {
            isValid = signature.verify(Base64.decodeBase64(new String(signBytes, charset)));
        } else {
            isValid = signature.verify(signBytes);
        }
        return isValid;
    }

}