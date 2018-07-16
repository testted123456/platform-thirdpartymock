package com.platform.apps.thirdpartymock.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KeyStore {

	private static String privateKey;
	private static String publicKey;

	public static void init() throws IOException {
		InputStream isv = KeyStore.class.getClassLoader().getResourceAsStream("pkcs8_rsa_private_key_2048.pem");
		BufferedReader reader = new BufferedReader(new InputStreamReader(isv));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append('\r');
		}
		privateKey = sb.toString();
		InputStream isp = KeyStore.class.getClassLoader().getResourceAsStream("rsa_public_key_2048.pem");
		BufferedReader read = new BufferedReader(new InputStreamReader(isp));
		StringBuilder sb2 = new StringBuilder();
		while ((line = read.readLine()) != null) {
			sb2.append(line);
			sb2.append('\r');
		}
		publicKey = sb2.toString();
	}

	public static String getPrivateKey() {
		return privateKey;
	}

	public static String getPublicKey() {
		return publicKey;
	}

}
