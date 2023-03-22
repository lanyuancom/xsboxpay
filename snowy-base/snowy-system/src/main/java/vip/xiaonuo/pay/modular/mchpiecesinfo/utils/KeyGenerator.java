package vip.xiaonuo.pay.modular.mchpiecesinfo.utils;

import java.security.NoSuchAlgorithmException;

/*
2015-01-23
*/
public class KeyGenerator {

	static public KeyPair generateKeyPair(KeyFormat format)
			throws NoSuchAlgorithmException {
		KeyPair keyPair = new KeyPair(format);

		return keyPair;
	}

	static public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		return generateKeyPair(KeyFormat.ASN);
	}
}
