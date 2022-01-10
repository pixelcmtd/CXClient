package de.chrissx.alts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AltCryptography {

	public static final int SHA_ROUNDS = 1 << 20;

	public static void encrypt(Path file, String password, byte[] b) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher ci = Cipher.getInstance("AES");
		ci.init(Cipher.ENCRYPT_MODE,
		        new SecretKeySpec(generateKey(password), "AES"));
		Files.write(file, ci.doFinal(b));
	}

	public static byte[] decrypt(Path file, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		Cipher ci = Cipher.getInstance("AES");
		ci.init(Cipher.DECRYPT_MODE,
		        new SecretKeySpec(generateKey(password), "AES"));
		return ci.doFinal(Files.readAllBytes(file));
	}

	static byte[] generateKey(String s) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] b = pad(s.getBytes(StandardCharsets.UTF_8), 6);
		byte[] o = b;
		Random r = newRand(b);
		for(int i = 0; i < SHA_ROUNDS; i++) {
			byte[] salt = new byte[r.nextInt(100)];
			r.nextBytes(salt);
			b = md.digest(doubleSalt(b, salt, o));
		}
		return b;
	}

	static Random newRand(byte[] b) {
		return new Random((b[0]<<40)|(b[1]<<32)|(b[2]<<24)|(b[3]<<16)|(b[4]<<8)|b[5]);
	}

	static byte[] pad(byte[] b, int l) {
		if(b.length >= l)
			return b;
		byte[] c = new byte[l];
		for(int i = 0; i < b.length; i++)
			c[i] = b[i];
		for(int i = b.length; i < l; i++)
			c[i] = (byte)(i ^ l);
		return c;
	}

	static byte[] doubleSalt(byte[] b, byte[] s1, byte[] s2) {
		byte[] c = new byte[b.length + s1.length + s2.length];
		for(int i = 0; i < b.length; i++)
			c[i] = b[i];
		for(int i = 0; i < s1.length; i++)
			c[i + b.length] = s1[i];
		for(int i = 0; i < s2.length; i++)
			c[i + b.length + s1.length] = s2[i];
		return c;
	}
}
