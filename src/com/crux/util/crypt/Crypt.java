package com.crux.util.crypt;
import com.crux.util.StringTools;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * En/De crypt procedures using AES algorithm Created by reza rudyanto Created
 * on June, 1st 2004
 * 
 * @author reza rudyanto
 * @version 1.0
 */
public class Crypt {
	/**
	 * Private constructor
	 */
	private Crypt() {
	}
	/**
	 * Generate Random Key
	 * 
	 * @return random byte array key
	 * @throws java.lang.Exception
	 */
	public static byte[] generateRandomKey() throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(DEFAULT_ALGORITHM);
		kgen.init(128); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey(); // Generate the secret key specs.
		byte[] raw = skey.getEncoded();
		return raw;
	}
	/**
	 * Generate Key from given pass phrase
	 * 
	 * @param pass
	 *            key to be used
	 * @return key generated
	 * @throws java.lang.Exception
	 */
	public static byte[] generateKey(String passKey) throws Exception {
		if (passKey == null) {
			throw new IllegalArgumentException("Pass Key must be supplied");
		}
		char[] arrKey = passKey.toCharArray();
		PBEKeySpec pbeKeySpec = new PBEKeySpec(arrKey);
		SecretKeyFactory factory =
			SecretKeyFactory.getInstance(DEFAULT_PBE_METHOD);
		SecretKey secretKey = factory.generateSecret(pbeKeySpec);
		return secretKey.getEncoded();
	}
	/**
	 * Generate 128 bit MD5 Key from given pass key
	 * 
	 * @param passKey
	 *            byte buffer password key
	 * @return byte buffer of 128 bit md5 key
	 * @throws java.lang.Exception
	 */
	public static byte[] generateMD5Key(byte[] passKey) throws Exception {
		if (passKey == null) {
			throw new IllegalArgumentException("Pass Key must be supplied");
		}
		MessageDigest md = MessageDigest.getInstance(DEFAULT_DIGEST);
		md.update(passKey);
		return md.digest();
	}
	/**
	 * Get cipher object
	 * 
	 * @param key
	 * @param mode,
	 *            Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
	 * @return cipher object
	 * @throws java.lang.Exception
	 */
	public static Cipher getCipher(byte[] key, int mode) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, DEFAULT_ALGORITHM);
		Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
		// Instantiate the cipher
		cipher.init(mode, skeySpec);
		return cipher;
	}
	/**
	 * Encrypt source byte array
	 * 
	 * @param key
	 * @param original
	 *            source array
	 * @return encrypted byte array
	 * @throws java.lang.Exception
	 */
	public static byte[] encrypt(byte[] key, byte[] source) throws Exception {
		if (key == null) {
			throw new IllegalArgumentException("Key must be supplied");
		}
		if (source == null) {
			throw new IllegalArgumentException("Source must be supplied");
		}
		return getCipher(key, Cipher.ENCRYPT_MODE).doFinal(source);
	}
	/**
	 * Decrypt source byte array
	 * 
	 * @param key
	 * @param encrypted
	 *            source array
	 * @return decrypted byte array
	 * @throws java.lang.Exception
	 */
	public static byte[] decrypt(byte[] key, byte[] source) throws Exception {
		if (key == null) {
			throw new IllegalArgumentException("Key must be supplied");
		}
		if (source == null) {
			throw new IllegalArgumentException("Source must be supplied");
		}
		return getCipher(key, Cipher.DECRYPT_MODE).doFinal(source);
	}
	/**
	 * Turns array of bytes into hexadecimal string
	 * 
	 * @param buf
	 *            Array of bytes to convert to hex string
	 * @return Generated hex string
	 */
	public static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		for (int i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10) {
				strbuf.append("0");
			}
			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}
		return strbuf.toString();
	}
	/**
	 * Convert String of Hexadecimal to Byte Buffer
	 * 
	 * @param source
	 *            source string
	 * @return byte buffer representation of this hexadecimal string
	 */
	public static byte[] asByteBuffer(String source) {
		byte[] result = new byte[source.length() / 2];
		for (int i = 0, j = 0; i < source.length(); i = i + 2) {
			String str = source.substring(i, i + 2);
			int byt = Integer.parseInt(str, 16);
			result[j++] = (byte) byt;
		}
		return result;
	}
	/**
	 * Generate Cipher output stream from given output stream and cipher
	 * 
	 * @param os
	 *            next output stream
	 * @param c
	 *            cipher to be used
	 * @return new cipher output stream
	 */
	public static OutputStream getCipherOutputStream(
		OutputStream os,
		Cipher c) {
		return new CipherOutputStream(os, c);
	}
	/**
	 * Generate Cipher input stream from given input stream and cipher
	 * 
	 * @param is
	 *            next input stream
	 * @param c
	 *            cipher to be used
	 * @return new cipher input stream
	 */
	public static InputStream getCipherInputStream(InputStream is, Cipher c) {
		return new CipherInputStream(is, c);
	}
	/**
	 * Default Algorithm
	 */
	public static final String DEFAULT_ALGORITHM = "AES";
	/**
	 * Default message digest
	 */
	public static final String DEFAULT_DIGEST = "MD5";
	/**
	 * Default Key Size in byte
	 */
	public static final int DEFAULT_KEY_SIZE = 16;
	/**
	 * Default Password Based Encryption Method
	 */
	public static final String DEFAULT_PBE_METHOD = "PBEWithMD5AndDES";
	/**
	 * Default Password Key
	 */
	public static final String DEFAULT_PASS_KEY = "SU3k@msel";
	/**
	 * Encrypt mode
	 */
	public static final int ENCRYPT = Cipher.ENCRYPT_MODE;
	/**
	 * Decrypt mode
	 */
	public static final int DECRYPT = Cipher.DECRYPT_MODE;

	public static void main(String[] args) throws Exception {


		System.out.println("tes : " + Crypt.asHex(Crypt.generateMD5Key(StringTools.getBytes("040120200613043000" + "/" + "05680224"))));

		String message = "05590163";

		System.out.println("source : " + message);
		byte[] en =
			encrypt(
				generateMD5Key(generateKey(DEFAULT_PASS_KEY)),
				message.getBytes());
		System.out.println("encrypted : " + en.toString());

		byte[] de = decrypt(generateMD5Key(generateKey(DEFAULT_PASS_KEY)), en);
		
                //System.out.println("decrypted : " + new String(de));
                System.out.println("decrypted aa : " + Digest.computeDigest("9e4b2479eace975be826d658e823f0be"));

		String fileName = String.valueOf(new java.util.Date().getTime());

		OutputStream os =
			getCipherOutputStream(
				new GZIPOutputStream(new FileOutputStream(fileName)),
				getCipher(
					generateMD5Key(generateKey(DEFAULT_PASS_KEY)),
					ENCRYPT));
		os.write(message.getBytes());
		os.flush();
		os.close();

		InputStream is =
			getCipherInputStream(
				new GZIPInputStream(new FileInputStream(fileName)),
				getCipher(
					generateMD5Key(generateKey(DEFAULT_PASS_KEY)),
					DECRYPT));

		char[] buff = new char[message.toCharArray().length];
		for (int data = 0, j = 0;(data = is.read()) != -1;) {
			buff[j++] = (char) data;
		}
		is.close();
		System.out.println(" from file : " + new String(buff));
	}
}