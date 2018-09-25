package com.platform.apps.thirdpartymock.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

public class SMCryptUtil {

	public static Logger logger = LoggerFactory.getLogger(SMCryptUtil.class);

	public static String smzfPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsPNh/QHhmdxxtTtnwtM0" +
			"Nkkod+JgOK3p5mMaABHOadcNjPuXb+Cnx+Fb1nPDrH/lg7h8owF1U686+TX2ocJ0" +
			"oug1bGsDkIYgLKzh+Y03NOo+4XkhCIEpByXUkAaiZo11Dta/NYFrJwhZXL96i0IR" +
			"1ELuhWaQXFQo+9NgRb3OuDpgjD45mBQoV+Pbmrl5oUVOYhCV8qfTfvD6ktc/nMc1" +
			"sj7ERkn39iCI/71rAAKJuu28woXTHA3nIibVYVBxw3nblKWXyQ6VKsTiwEm2+UR2" +
			"75n263YjT44JUjVXRxoqlhrn7bpSc9xcOaiV51h98MZiC/+GZf8QuKLEwjG5Xcj3" +
			"YQIDAQAB";

	public static String smzfPriKey = 
			"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCw82H9AeGZ3HG1" +
			"O2fC0zQ2SSh34mA4renmYxoAEc5p1w2M+5dv4KfH4VvWc8Osf+WDuHyjAXVTrzr5" +
			"NfahwnSi6DVsawOQhiAsrOH5jTc06j7heSEIgSkHJdSQBqJmjXUO1r81gWsnCFlc" +
			"v3qLQhHUQu6FZpBcVCj702BFvc64OmCMPjmYFChX49uauXmhRU5iEJXyp9N+8PqS" +
			"1z+cxzWyPsRGSff2IIj/vWsAAom67bzChdMcDeciJtVhUHHDeduUpZfJDpUqxOLA" +
			"Sbb5RHbvmfbrdiNPjglSNVdHGiqWGuftulJz3Fw5qJXnWH3wxmIL/4Zl/xC4osTC" +
			"MbldyPdhAgMBAAECggEAf1MRfhE9eQ3TT/nn5ps8pZcspF7ip8YUWO+URNqLMUNJ" +
			"/9kFFb6MW9n/0CbKdLQuvQIhxNUGzGl5QNsglTKr5JC76EB0dyjbu+nGTF/dipjH" +
			"GEylhU4S7I6/DceIUcyVn1DMkz6FkLeEPaWWEuA+ngJvSNQNKjJwrwtg6a3zMkG0" +
			"TBIeqhd5rpEuLH63erahmD/2tQmMHcyxpK7icmSVf4FuuqW0ZGYRTE+AAY2LxrFj" +
			"d5oPpr5YqYMeYyty0b71JoVXPwF/2pQZ0oOITOiXkgi10VXufXauu+G3R0zuaZeA" +
			"64PSmYIDf6giqei/VQKxVnhCbRzuuS5QKYMTvC7oAQKBgQDi6/LGd06FWFvT7F9v" +
			"TSApUeLxPimk1GJ3vzV8HKEZEpRsnHm2avVNGt/Y99i8AsWdqm3lW5TzOW9utqOi" +
			"/FgsdTLPzgDUDy6sWaiIJczTTuUC3ZUDVJPOdmr4cJyp53EiNkh9996v4RAgUkxX" +
			"P+NFzvNwVXxyJhHb7sRZwc5UQQKBgQDHoCmEOilCcYuN+5jP738zCuz89B2WzBRs" +
			"VWmPVb7OArTeC1e720WbrOyS1cV2r4ClX1eKYYqPqfZNuzIEP86J/7yfjpAkGAJv" +
			"tqg/IwZwFwdZvnACMk+d0Ja119dBqdOhS6YbhxFIFIst1MiGWzu/cJg7wPZARpz/" +
			"TTJQRv1bIQKBgFg0In/mbA0E1VzAqi/XKMMm4zGmWgWJ0GR/XEjL0Aqw4CR/hgfT" +
			"91lrMsthO0uJQEsSrfjS6kjIcHn2YBDASX2uaHvBtSy4Ygn4J7wUBVO1fOrqOju7" +
			"KmC8QsQi8aJRJfaIZxEfwd1XfC80Dhxz5uGBmbp2YBL2nUaBvqys2oiBAoGBAI5r" +
			"+VLKQAeUhNFekG7B2wlqoRvzemzM20FTSsWlkkTeb3X/t69iSG5B2GMGOyRrIChc" +
			"NlOXNck7pGtew9d+Qnf4SfW4O6RUsOBM/ApXjc9Izsi7cqROirj4SZ4hCQVjCYcJ" +
			"NtBq8+xuI8ImDo605pK13Ra8m7PN7VLEG8Jkc3uhAoGBAJ4a62oaI8y/tWDUGn4g" +
			"FQlO99birtj1RJLLt+7RCwyoaQNFPLeH8fOMZDyzfA5v3yBI6BtSEgRSlGbGpyCo" +
			"Z5d+8HEIpsMb8hUS/uIFAuiuuDYIS20V+Ws7D6k8uH7yT89d5vFNgsyaMn05JERk" +
			"HHTmCsY2bqPWjrJvBw3C7CXW";

	/** 生成密钥的字符集合(只取char中的数字和字母) */
	private static final char[] KEY_CHAR_ARRAY = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z' };

	/**
	 * getKeys:创建X509EncodedKeySpec 对象. <br/>
	 * 方法名： getKeys.<br/>
	 * 
	 * @throws Exception
	 *             参数或异常：@param para 参数 参数或异常：@throws QTException .<br/>
	 * 
	 */
	public static Map<String, Object> getKeys(Map<String, Object> para) throws Exception {
		/** 瑞银信的公钥 */
		String cooperPublickey = (String) para.get("publickKey");
		PublicKey pubKey = null;
		PrivateKey priKey = null;
		try {
			/** 私钥 */
			String privateKey = (String) para.get("privateKey");
			X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(cooperPublickey));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			pubKey = keyFactory.generatePublic(pubX509);

			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory priKeyFcty = KeyFactory.getInstance("RSA");
			priKey = priKeyFcty.generatePrivate(priPKCS8);
		} catch (Exception e) {
			logger.error("生成密钥对象异常:" + e.getMessage(), e);
			throw new Exception("生成密钥对象异常");
			// throw new QTException(Console_ErrCode.RESP_CODE_88_ERR_TXN,
			// "生成密钥对象异常", "生成密钥对象异常");
		}

		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("hzfPriKey", priKey);
		keys.put("yhPubKey", pubKey);
		return keys;
	}

	/**
	 * 
	 * 【方法名】 : 解密返回报文. <br/>
	 * 【作者】: Administrator .<br/>
	 * 【时间】： 2016年11月10日 下午3:32:55 .<br/>
	 * 【参数】： .<br/>
	 * 
	 * @param dataStr
	 *            数据
	 * @param hzfPriKey
	 *            私钥
	 * @return 返回解密结果
	 * @throws QTException
	 *             解密异常<br/>
	 */
	public static String decryptData(String dataStr, PrivateKey hzfPriKey) throws Exception {
		String reqStr = "";
		try {
			JSONObject jsonObject = JSONObject.parseObject(dataStr);
			String reqEncryptData = jsonObject.getString("encryptData");
			String reqEncryptKey = jsonObject.getString("encryptKey");
			byte[] decodeBase64KeyBytes = Base64.decodeBase64(reqEncryptKey.getBytes("UTF-8"));
			// 解密encryptKey得到merchantAESKey
			byte[] merchantAESKeyBytes;
			merchantAESKeyBytes = RSADecrypt(decodeBase64KeyBytes, hzfPriKey, 2048, 11, "RSA/ECB/PKCS1Padding");

			// 使用base64解码商户请求报文
			byte[] decodeBase64DataBytes = Base64.decodeBase64(reqEncryptData.getBytes("UTF-8"));
			// 用解密得到的merchantAESKey解密encryptData
			byte[] merchantXmlDataBytes = AESDecrypt(decodeBase64DataBytes, merchantAESKeyBytes, "AES",
					"AES/ECB/PKCS5Padding", null);
			reqStr = new String(merchantXmlDataBytes, "UTF-8");
			logger.info("扫码支付请求明文]：" + reqStr);
		} catch (Exception e) {
			logger.error("扫码支付请求数据异常：" + e.getMessage(), e);
		}
		return reqStr;
	}

	public static String decryptData(String encryptData, String encryptKey) {
		/** 编码公私钥对象 */
		Map<String, Object> keyMaps = new HashMap<String, Object>();
		keyMaps.put("publickKey", smzfPubKey);
		keyMaps.put("privateKey", smzfPriKey);

		Map<String, Object> keys = null;
		try {
			keys = SMCryptUtil.getKeys(keyMaps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrivateKey hzfPriKey = (PrivateKey) keys.get("hzfPriKey");
		PublicKey yhPubKey = (PublicKey) keys.get("yhPubKey");

		try {
			byte[] decodeBase64KeyBytes = Base64.decodeBase64(encryptKey.getBytes("UTF-8"));
			// 解密encryptKey得到merchantAESKey
			byte[] merchantAESKeyBytes = SMCryptUtil.RSADecrypt(decodeBase64KeyBytes, hzfPriKey, 2048, 11,
					"RSA/ECB/PKCS1Padding");

			// 使用base64解码商户请求报文
			byte[] decodeBase64DataBytes = Base64.decodeBase64(encryptData.getBytes("UTF-8"));

			// 用解密得到的merchantAESKey解密encryptData
			byte[] merchantXmlDataBytes = SMCryptUtil.AESDecrypt(decodeBase64DataBytes, merchantAESKeyBytes, "AES",
					"AES/ECB/PKCS5Padding", null);
			String data = new String(merchantXmlDataBytes, "UTF-8");
			logger.info("解密后的数据：" + data);

			return data;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String getEncrtptKey() throws Exception {
		String keyStr = getAESKey();
		/** 生成AES密钥key字节流 */
		byte[] keyBytes = keyStr.getBytes("UTF-8");
		Map<String, Object> keyMaps = new HashMap<String, Object>();
		keyMaps.put("publickKey", smzfPubKey);
		keyMaps.put("privateKey", smzfPriKey);
		Map<String, Object> keys = getKeys(keyMaps);
		PublicKey yhPubKey = (PublicKey) keys.get("yhPubKey");
		String encrtptKey = new String(
				Base64.encodeBase64(SMCryptUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding")),
				"UTF-8");

		return encrtptKey;
	}

	/**
	 * RSA解密
	 * 
	 * @param encryptedBytes
	 *            加密后字节数组
	 * @param privateKey
	 *            私钥
	 * @param keyLength
	 *            密钥bit长度
	 * @param reserveSize
	 *            padding填充字节数，预留11字节
	 * @param cipherAlgorithm
	 *            加解密算法，一般为RSA/ECB/PKCS1Padding
	 * @return 解密后字节数组，不经base64编码
	 * @throws ServiceException
	 *             000
	 */
	public static byte[] RSADecrypt(byte[] encryptedBytes, PrivateKey privateKey, int keyLength, int reserveSize,
			String cipherAlgorithm) throws Exception {
		int keyByteSize = keyLength / 8;
		int decryptBlockSize = keyByteSize - reserveSize;
		int nBlock = encryptedBytes.length / keyByteSize;
		ByteArrayOutputStream outbuf = null;
		try {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			outbuf = new ByteArrayOutputStream(nBlock * decryptBlockSize);
			for (int offset = 0; offset < encryptedBytes.length; offset += keyByteSize) {
				int inputLen = encryptedBytes.length - offset;
				if (inputLen > keyByteSize) {
					inputLen = keyByteSize;
				}

				byte[] decryptedBlock = cipher.doFinal(encryptedBytes, offset, inputLen);
				outbuf.write(decryptedBlock);
			}

			outbuf.flush();
			outbuf.close();
			return outbuf.toByteArray();
		} catch (NoSuchAlgorithmException e) {
			logger.error(String.format("数字签名时没有[%s]此类算法", cipherAlgorithm));
		} catch (NoSuchPaddingException e) {
			logger.error(String.format("数字签名时没有[%s]此类算法", cipherAlgorithm));
		} catch (InvalidKeyException e) {
			logger.error("无效密钥");
		} catch (IllegalBlockSizeException e) {
			logger.error("加密块大小不合法");
		} catch (BadPaddingException e) {
			logger.error("错误填充模式");
		} catch (IOException e) {
			logger.error("字节输出流异常");
		}
		return null;
	}

	/**
	 * AES解密
	 * 
	 * @param encryptedBytes
	 *            密文字节数组，不经base64编码
	 * @param keyBytes
	 *            密钥字节数组
	 * @param keyAlgorithm
	 *            密钥算法
	 * @param cipherAlgorithm
	 *            加解密算法
	 * @param iv
	 *            随机向量
	 * @return 解密后字节数组
	 * @throws ServiceException
	 *             异常
	 */
	public static byte[] AESDecrypt(byte[] encryptedBytes, byte[] keyBytes, String keyAlgorithm, String cipherAlgorithm,
			String iv) throws Exception {
		try {
			// AES密钥长度为128bit、192bit、256bit，默认为128bit
			if (keyBytes.length % 8 != 0 || keyBytes.length < 16 || keyBytes.length > 32) {
				throw new Exception("AES密钥长度不合法");
			}

			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			SecretKey secretKey = new SecretKeySpec(keyBytes, keyAlgorithm);
			if (iv != null && iv.trim() != null) {
				iv = iv.trim();
				IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
				cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
			}

			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			return decryptedBytes;
		} catch (NoSuchAlgorithmException e) {
			logger.error(String.format("没有[%s]此类加密算法", cipherAlgorithm), e);
		} catch (NoSuchPaddingException e) {
			logger.error(String.format("没有[%s]此类填充模式", cipherAlgorithm), e);
		} catch (InvalidKeyException e) {
			logger.error("无效密钥", e);
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("无效密钥参数", e);
		} catch (BadPaddingException e) {
			logger.error("错误填充模式", e);
		} catch (IllegalBlockSizeException e) {
			logger.error("加密块大小不合法", e);
		}
		return null;
	}

	/**
	 * 
	 * getAESKey:获取AES密钥，16位的字母数字组合. <br/>
	 * 
	 * @author CAOWENJUN
	 * @return AES
	 * @since JDK 1.6
	 */
	public static String getAESKey() {

		StringBuffer keyBuffer = new StringBuffer("");
		for (int i = 0; i < 16; i++) {
			keyBuffer.append(KEY_CHAR_ARRAY[(int) Math.floor(Math.random() * KEY_CHAR_ARRAY.length)]);
		}
		return keyBuffer.toString();
	}

	/**
	 * AES加密
	 * 
	 * @param plainBytes
	 *            明文字节数组
	 * @param keyBytes
	 *            密钥字节数组
	 * @param keyAlgorithm
	 *            密钥算法
	 * @param cipherAlgorithm
	 *            加解密算法
	 * @param iv
	 *            随机向量
	 * @return 加密后字节数组，不经base64编码
	 * @throws ServiceException
	 *             异常处理
	 */
	public static byte[] AESEncrypt(byte[] plainBytes, byte[] keyBytes, String keyAlgorithm, String cipherAlgorithm,
			String iv) throws Exception {
		byte[] encryptedBytes = null;
		try {
			if (keyBytes.length % 8 != 0 || keyBytes.length < 16 || keyBytes.length > 32) {
				logger.error("AES密钥长度不合法");
			}
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			SecretKey secretKey = new SecretKeySpec(keyBytes, keyAlgorithm);
			if (iv != null && iv.trim() != null) {
				iv = iv.trim();
				IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			}
			encryptedBytes = cipher.doFinal(plainBytes);
		} catch (NoSuchAlgorithmException e) {
			logger.error(String.format("没有[%s]此类加密算法", cipherAlgorithm));
		} catch (NoSuchPaddingException e) {
			logger.error(String.format("没有[%s]此类填充模式", cipherAlgorithm));
		} catch (InvalidKeyException e) {
			logger.error("无效密钥");
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("无效密钥参数");
		} catch (BadPaddingException e) {
			logger.error("错误填充模式");
		} catch (IllegalBlockSizeException e) {
			logger.error("加密块大小不合法");
		}

		return encryptedBytes;
	}

	/**
	 * 数字签名函数入口
	 * 
	 * @param plainBytes
	 *            待签名明文字节数组
	 * @param privateKey
	 *            签名使用私钥
	 * @param signAlgorithm
	 *            签名算法
	 * @return 签名后的字节数组
	 * @throws ServiceException
	 *             异常
	 */
	public static byte[] digitalSign(byte[] plainBytes, PrivateKey privateKey, String signAlgorithm) throws Exception {
		byte[] signBytes = null;
		try {
			Signature signature = Signature.getInstance(signAlgorithm);
			signature.initSign(privateKey);
			signature.update(plainBytes);
			signBytes = signature.sign();
		} catch (NoSuchAlgorithmException e) {
			logger.error(String.format("数字签名时没有[%s]此类算法", signAlgorithm));
		} catch (InvalidKeyException e) {
			logger.error("数字签名时私钥无效");
		} catch (SignatureException e) {
			logger.error("数字签名时出现异常");
		}
		return signBytes;
	}

	/**
	 * RSA加密
	 * 
	 * @param plainBytes
	 *            明文字节数组
	 * @param publicKey
	 *            公钥
	 * @param keyLength
	 *            密钥bit长度
	 * @param reserveSize
	 *            padding填充字节数，预留11字节
	 * @param cipherAlgorithm
	 *            加解密算法，一般为RSA/ECB/PKCS1Padding
	 * @return 加密后字节数组，不经base64编码
	 * @throws ServiceException
	 *             000
	 */
	public static byte[] RSAEncrypt(byte[] plainBytes, PublicKey publicKey, int keyLength, int reserveSize,
			String cipherAlgorithm) throws Exception {
		int keyByteSize = keyLength / 8;
		int encryptBlockSize = keyByteSize - reserveSize;
		int nBlock = plainBytes.length / encryptBlockSize;
		if ((plainBytes.length % encryptBlockSize) != 0) {
			nBlock += 1;
		}
		ByteArrayOutputStream outbuf = null;
		try {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			outbuf = new ByteArrayOutputStream(nBlock * keyByteSize);
			for (int offset = 0; offset < plainBytes.length; offset += encryptBlockSize) {
				int inputLen = plainBytes.length - offset;
				if (inputLen > encryptBlockSize) {
					inputLen = encryptBlockSize;
				}
				byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
				outbuf.write(encryptedBlock);
			}
			outbuf.flush();
			outbuf.close();
			return outbuf.toByteArray();
		} catch (NoSuchAlgorithmException e) {
			logger.error(String.format("数字签名时没有[%s]此类算法", cipherAlgorithm));
		} catch (NoSuchPaddingException e) {
			logger.error(String.format("没有[%s]此类填充模式", cipherAlgorithm));
		} catch (InvalidKeyException e) {
			logger.error("无效密钥");
		} catch (IllegalBlockSizeException e) {
			logger.error("加密块大小不合法");
		} catch (BadPaddingException e) {
			logger.error("错误填充模式");
		} catch (IOException e) {
			logger.error("字节输出流异常");
		}
		return null;
	}

	/**
	 * 加密请求数据
	 * 
	 * @param jsonData
	 * @return
	 */
	public static Map<String, String> encryptData(JSONObject jsonData) throws Exception {
		byte[] reqDataByte = jsonData.toJSONString().getBytes("UTF-8");
		
		Map<String, Object> keyMaps = new HashMap<String, Object>();
		keyMaps.put("publickKey", SMCryptUtil.smzfPubKey);
		keyMaps.put("privateKey", SMCryptUtil.smzfPriKey);
		Map<String, Object> keys = null;
		
		try {
			keys = SMCryptUtil.getKeys(keyMaps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		PrivateKey hzfPriKey = (PrivateKey) keys.get("hzfPriKey");
        PublicKey yhPubKey = (PublicKey) keys.get("yhPubKey");
		 
        /** 生成AES密钥key */
        String keyStr = SMCryptUtil.getAESKey();
       
        /** 生成AES密钥key字节流 */
        byte[] keyBytes = keyStr.getBytes("UTF-8");
       
        /** 对请求数据进行AES加密，并进行base64编码 */
        String encryptReqData = new String(Base64.encodeBase64(SMCryptUtil.AESEncrypt(reqDataByte, keyBytes, "AES", "AES/ECB/PKCS5Padding", null)), "UTF-8");
       
        /** 对请求数据进行哈希签名RSA加密，并进行base64编码 */
        String signReqData = new String(Base64.encodeBase64(SMCryptUtil.digitalSign(reqDataByte, hzfPriKey, "SHA1WithRSA")), "UTF-8");
       
        /** 对AES进行RSA加密，并进行base64编码 */
        String encrtptKey = new String(Base64.encodeBase64(SMCryptUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding")), "UTF-8");
       
        Map<String, String> map = new HashMap<>();
        map.put("encryptData", encryptReqData);
        map.put("signData", signReqData);
        map.put("encryptKey", encrtptKey);
        
        return map;
	}
	
	public static String signData(JSONObject jsonData) throws Exception{
		Map<String, Object> keys = getKeys();
		PrivateKey hzfPriKey = (PrivateKey) keys.get("hzfPriKey");
		byte[] reqDataByte = jsonData.toJSONString().getBytes("UTF-8");
		
		/** 对请求数据进行哈希签名RSA加密，并进行base64编码 */
        String signReqData = new String(Base64.encodeBase64(digitalSign(reqDataByte, hzfPriKey, "SHA1WithRSA")), "UTF-8");
        
		return signReqData;
	}
	
	public static Map<String, Object> getKeys() throws Exception {
        Map<String, Object> keyMaps = new HashMap<String, Object>();
        keyMaps.put("publickKey", smzfPubKey);
        keyMaps.put("privateKey", smzfPriKey);
        Map<String, Object> keys = null;
		keys = getKeys(keyMaps);
        return keys;
	}

}
