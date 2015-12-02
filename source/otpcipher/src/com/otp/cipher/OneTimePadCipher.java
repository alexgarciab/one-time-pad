package com.otp.cipher;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Class used to cipher messages using an one time generated key
 * 
 * @author Alejandro García
 *
 */
public class OneTimePadCipher {
	private String CHARSET = "UTF-8";

	/**
	 * Encrypts a String as UTF-8 bytes given a hexadecimal key.
	 * 
	 * @param keyString
	 *            Hex key
	 * @param input
	 *            Message to encrypt
	 * @return byte array
	 * @throws Exception
	 *             Exception
	 */
	public byte[] encrypt(String keyString, String input) throws Exception {
		if (input == null) {
			throw new Exception("Cannot encrypt null plain text");
		}
		if (keyString == null) {
			throw new Exception("Cannot encrypt with a null key name");
		}
		// convert String to UTF-8 bytes
		byte[] inputBytes = null;
		try {
			inputBytes = input.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
		// get the appropriate hash
		String hash = generateHash(keyString, input.length());
		byte[] key = null;
		try {
			key = hash.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
		byte[] encrypted = encrypt(inputBytes, key);
		return encrypted;
	}

	/**
	 * Decrypts the provided bytes into a Java String given the name of a key.
	 * Assumes the data UTF-8 bytes.
	 * 
	 * @param keyname
	 *            Hex key
	 * @param input
	 *            byte array to decrypt
	 * @return Message
	 * @throws Exception
	 *             Exception
	 */
	public String decrypt(String keyname, byte[] input) throws Exception {
		// decrypt
		// get the appropriate key data
		String hash = generateHash(keyname, input.length);
		byte[] key = null;
		try {
			key = hash.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
		byte[] decrypted = decrypt(input, key);
		// interpret the result as UTF-8 bytes
		String result = null;
		try {
			result = new String(decrypted, CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
		return result;
	}

	/**
	 * Encrypt a message with a given key
	 * 
	 * @param plaintext
	 *            Text message
	 * @param key
	 *            Generated Key
	 * @return Encrypted message
	 * @throws Exception
	 *             Exception
	 */
	public byte[] encrypt(byte[] plaintext, byte[] key) throws Exception {
		if (plaintext == null) {
			throw new Exception("Cannot encrypt null plain text");
		}
		if (key == null) {
			throw new Exception("Cannot encrypt with a null key");
		}
		if (plaintext.length != key.length) {
			throw new Exception("Cannot encrypt, input byte length ["
					+ plaintext.length
					+ "] is not the same as the key length [" + key.length
					+ "]");
		}
		byte[] result = new byte[plaintext.length];
		for (int i = 0; i < key.length; i++) {
			result[i] = (byte) (plaintext[i] ^ key[i]);
		}
		return result;
	}

	/**
	 * Decrypt a byte array using a given key
	 * 
	 * @param ciphertext
	 *            Encrypted text
	 * @param key
	 *            Generated key
	 * @return Decrypted message
	 * @throws Exception
	 *             Exception
	 */
	public byte[] decrypt(byte[] ciphertext, byte[] key) throws Exception {
		if (ciphertext == null) {
			throw new Exception("Cannot decrypt null cipher text");
		}
		if (key == null) {
			throw new Exception("Cannot decrypt with a null key");
		}
		if (ciphertext.length != key.length) {
			throw new Exception("Cannot decrypt, input byte length ["
					+ ciphertext.length
					+ "] is not the same as the key length [" + key.length
					+ "]");
		}
		byte[] result = new byte[ciphertext.length];
		for (int i = 0; i < key.length; i++) {
			result[i] = (byte) (ciphertext[i] ^ key[i]);
		}
		return result;
	}

	/**
	 * Generate a random string of a given length
	 * 
	 * @param key
	 *            Hex Key
	 * @param length
	 *            length of the string
	 * @return Random String
	 */
	private static String generateHash(String key, int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		long l = Long.decode("0x" + key);
		Random random = new Random(l);
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}
}
