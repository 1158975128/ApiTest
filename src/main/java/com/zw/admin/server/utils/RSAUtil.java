package com.zw.admin.server.utils;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

@SuppressWarnings("restriction")
public class RSAUtil {

	static String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApOY9IhOlzcDDp0Bc61Yau+dmc71wmqeilWJ42TlVD3q0bR1UQOXpwIo49rW7XWtt1pn5FUn6pjKcbgkvEPyvHr89+7DsxhcwkJOcuotxkK/dKFhTcpBMMDrfZxQu7hJCBQWNrK0ghwz/9CiV6DiIKOlNZh+K+lAQg3NudI01wlodoOFZCHy4FFGFuuHOb7va+Cf2jRxMw2hhI8V+wDDqktyI9j1HE2R54gbsNWFBoqEmJGTKivZ+gCHmjU3cTDyjduRgZ6/f3Yu/AdU2p8tZxlXDl6yaYIyxTu+c5QejYomanG33GtXijgK+BagPdtX8hSVadlPzm2WVLvhhpzKf6wIDAQAB";
	static String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCk5j0iE6XNwMOnQFzrVhq752ZzvXCap6KVYnjZOVUPerRtHVRA5enAijj2tbtda23WmfkVSfqmMpxuCS8Q/K8evz37sOzGFzCQk5y6i3GQr90oWFNykEwwOt9nFC7uEkIFBY2srSCHDP/0KJXoOIgo6U1mH4r6UBCDc250jTXCWh2g4VkIfLgUUYW64c5vu9r4J/aNHEzDaGEjxX7AMOqS3Ij2PUcTZHniBuw1YUGioSYkZMqK9n6AIeaNTdxMPKN25GBnr9/di78B1Tany1nGVcOXrJpgjLFO75zlB6NiiZqcbfca1eKOAr4FqA921fyFJVp2U/ObZZUu+GGnMp/rAgMBAAECggEAB4c7Y0LiXvJd/FTD6TqVHd8CnmYXrffZRFiqi8BCUeKYOiMMpanZciDdWOYTF8C/0vxUktQ9zTiiS6O/slbW5+BzQ/pf1OtaHD+g+CNFnAVrY1wk8yYDVHBboqkZO2U5/mCyhx+gTIobZVo0/dUW4kdEcVUfUy40ksvCTy/jNGw1yq3iPYO8ln2iJJkkO8PNGpICc986ETXSzq/jHZbOzcDlGHIAoMrLEQxxzt1qRyDkkqbaXSa7eZ7iUrI93DkomDDqkDkM1ns7UJ1v9pafWVPu72OldTdH4/oJO8MbECh5/C+DUelKiEjWhcCxbpyKkyXcnTmk9aR5BSkoz3g9gQKBgQDyFI0v7SpZ3lG3d1mgTtSrzlUlzu6nveOk0PJwn/pdFAPE4I9lLjQDAe/9wbjyTA0RCgIvRcHyQJwOv2Hrpm6VDRRQg/+3dSrTCcLO73dK8mG7wAQzRX2VagbfCBPc/tbweqkm1yOwanEJq2k1/GPbJjceTZYC0kkwV/8Tevob1wKBgQCuYZNlvFm3sFVQIFeClfK/C8geRZBjRd6+rPJjOAF4kgnOPMW/bnq+axVRa4KQqSF0oO/eAggAlOHpd0j7uxutGZkRKTJ2etqbjelJQghBfA60DNPLpvlz6jhhpSMa/LikQc/OxgodeChlEnDjHuW1P2gYTStFuT67oWWg8UO6DQKBgQCoU4YA50ZliuMDUWfuwiG6/RPT4FmuJametvN9QKAUDJC6WvMahdAAr14y8nspb5w+VtlCCZ1cV3SsBIOQGgAWgCu5+K7c4AnRCJ5+yaAHaxZuOIwzIoS6WilA+BrntgO632+y5DGav8it0pSrxz2f6qi013oRqCrgBLAG2aIDrQKBgHH7G5IzE92KJ6lxCmIzyvBvLgYDaEZLRDYT63dJA0UhB8HDXWosN5TBf7Sxdi1CL7kA/Oh7IWnn5uZobs8D1SpHAhMCf5aL5NoqXyLgQ28yPJYZkFKcOI84uFOy0ghwyeurjL5WGgR7JsQx0pHR/0mCQXMo9bDkTYntC/Wv3Zn9AoGAI6dTTUQkKvMRsZ+Xmp6L2Vcv3MXxdA9cKqNl7aCQ4QXaOpQcWo/jnEvYF9QwBxcbg2ri9xxI8jz+QtMBblZzfGLJT27GUywo8Qxk+jHxAMZ8rT8dUX8aWwArJPVHa9tBLucYfusqvbKcq6qwzbQOLJRUcqC+9clglEMpjrukuJs=";

	public static String[] test() throws Exception {
		KeyPair keyPair = getKeyPair();
		String publicKeyStr = RSAUtil.getPublicKey(keyPair);
		String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
		String[] test = { publicKeyStr, privateKeyStr };
		return test;
	}

	/**
	 * 私钥解密
	 * 
	 * @param byte2Base64
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String byte2Base64) throws Exception {

		String privateDecryptStr = "";
		try {

			// ===================服务端================
			// 将Base64编码后的私钥转换成PrivateKey对象

			PrivateKey privateKey = RSAUtil.string2PrivateKey(privateKeyStr);
			// 加密后的内容Base64解码
			byte[] base642Byte = RSAUtil.base642Byte(byte2Base64);
			// 用私钥解密
			byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
			// 解密后的明文

			// privateDecryptStr = new String(privateDecrypt, "UTF8");
			for (byte b : privateDecrypt) {
				if (b != 0) {
					privateDecryptStr += (char) b;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return privateDecryptStr;

	}

	/**
	 * 公钥加密
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String password) throws Exception {

		// =================客户端=================

		// 将Base64编码后的公钥转换成PublicKey对象
		PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
		// 用公钥加密
		byte[] publicEncrypt = RSAUtil.publicEncrypt(password.getBytes(), publicKey);
		// 加密后的内容Base64编码
		String byte2Base64 = RSAUtil.byte2Base64(publicEncrypt);
		// System.out.println("公钥加密并Base64编码的结果：" + byte2Base64);

		return byte2Base64;
	}

	// 生成秘钥对
	public static KeyPair getKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

	// 获取公钥(Base64编码)
	public static String getPublicKey(KeyPair keyPair) {
		PublicKey publicKey = keyPair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		return byte2Base64(bytes);
	}

	// 获取私钥(Base64编码)
	public static String getPrivateKey(KeyPair keyPair) {
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return byte2Base64(bytes);
	}

	// 将Base64编码后的公钥转换成PublicKey对象
	public static PublicKey string2PublicKey(String pubStr) throws Exception {
		byte[] keyBytes = base642Byte(pubStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	// 将Base64编码后的私钥转换成PrivateKey对象
	public static PrivateKey string2PrivateKey(String priStr) throws Exception {
		byte[] keyBytes = base642Byte(priStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	// 公钥加密
	public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}

	// 私钥解密
	public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}

	// 字节数组转Base64编码
	public static String byte2Base64(byte[] bytes) {
		String encodeToString = Base64.getEncoder().encodeToString(bytes);
		return encodeToString;
	}

	// Base64编码转字节数组
	public static byte[] base642Byte(String base64Key) throws IOException {
		byte[] decode = Base64.getDecoder().decode(base64Key);
		return decode;
	}

}
