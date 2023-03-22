package vip.xiaonuo.pay.modular.mchpiecesinfo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author elson
 *
 */
public final class MD5Util {
	
	private MD5Util() {

	}

	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * Converts an array of bytes into an array of characters representing the
	 * hexidecimal values of each byte in order. The returned array will be
	 * double the length of the passed array, as it takes two characters to
	 * represent any given byte.
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @return A char[] containing hexidecimal characters
	 */
	private static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	private static MessageDigest getMD5Digest() {
		try {
			MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
			md5MessageDigest.reset();
			return md5MessageDigest;
		} catch (NoSuchAlgorithmException nsaex) {
			return null;
		}
	}

	/**
	 * ����content��md5ժҪ.
	 * 
	 * @param content
	 * @return md5���
	 */
	public static String md5(String content) {
		try {
			byte[] data = getMD5Digest().digest(content.getBytes("GBK"));
			char[] chars = encodeHex(data);
			return new String(chars);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String md5UTF8(String content) {
		try {
			byte[] data = getMD5Digest().digest(content.getBytes("UTF-8"));
			char[] chars = encodeHex(data);
			return new String(chars);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * ����content��md5ժҪ.
	 * 
	 * @param content
	 * @return md5���
	 */
	public static String md5GBK(String content) {
		try {
			byte[] data = getMD5Digest().digest(content.getBytes("GBK"));
			char[] chars = encodeHex(data);
			return new String(chars);
		} catch (Exception ex) {
			return null;
		}
	}

}