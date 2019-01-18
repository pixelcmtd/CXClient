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

	public static void encrypt(Path file, String password, byte[] b) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException
	{
		Cipher ci = Cipher.getInstance("AES");
		ci.init(Cipher.ENCRYPT_MODE,
			new SecretKeySpec(generateKey(password), "AES"));
		Files.write(file, ci.doFinal(b));
	}

	public static byte[] decrypt(Path file, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
	{
		Cipher ci = Cipher.getInstance("AES");
		ci.init(Cipher.DECRYPT_MODE,
			new SecretKeySpec(generateKey(password), "AES"));
		return ci.doFinal(Files.readAllBytes(file));
	}

	static byte[] generateKey(String s) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] b = pad(s.getBytes(StandardCharsets.UTF_8), 6);
		byte[] o = b;
		Random r = newRand(b);
		for(int i = 0; i < 1024; i++)
		{
			byte[] salt = new byte[r.nextInt(100)];
			r.nextBytes(salt);
			b = md.digest(doubleSalt(b, salt, o));
		}
		return b;
	}

	static Random newRand(byte[] b)
	{
		return new Random((b[0]<<40)|(b[1]<<32)|(b[2]<<24)|(b[3]<<16)|(b[4]<<8)|b[5]);
	}

	static byte[] pad(byte[] b, int l)
	{
		if(b.length >= l)
			return b;
		byte[] c = new byte[l];
		for(int i = 0; i < b.length; i++)
			c[i] = b[i];
		for(int i = b.length; i < l; i++)
			c[i] = (byte)(i ^ l);
		return c;
	}

	static byte[] doubleSalt(byte[] b, byte[] s1, byte[] s2)
	{
		byte[] c = new byte[b.length + s1.length + s2.length];
		for(int i = 0; i < b.length; i++)
			c[i] = b[i];
		for(int i = 0; i < s1.length; i++)
			c[i + b.length] = s1[i];
		for(int i = 0; i < s2.length; i++)
			c[i + b.length + s1.length] = s2[i];
		return c;
	}

	////old stuff,
	////because why shouldnt we keep it
	////7/**
	////7 * 
	////7 * @param a random string to use for key-creation
	////7 * @return The key
	////7 * @throws UnsupportedEncodingException
	////7 * @throws NoSuchAlgorithmException
	////7 */
	////7public static SecretKeySpec createKey(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
	////7	return new SecretKeySpec(generateKey(s), "AES");
	////7}
    ////7
	////7static String altToCsv(Alt a) {
	////7	return a.getEmail() + ";:;" + a.getPassword();
	////7}
    ////7
	////7static Alt csvToAlt(String csv) {
	////7	String[] splt = csv.split(";:;");
	////7	return new Alt(splt[0], splt[1]);
	////7}
    ////7
	////7static byte[] altsToBase64(List<Alt> alts) {
	////7	if(alts.size() == 0)
	////7		return new byte[0];
	////7	String s = altToCsv(alts.get(0));
	////7	for(int i = 1; i < alts.size(); i++)
	////7		s += "\u000b" + alts.get(i);
	////7	return Base64.getEncoder().encode(s.getBytes(StandardCharsets.UTF_8));
	////7}
    ////7
	////7static List<Alt> base64ToAlts(byte[] base64) {
	////7	if(base64.length == 0)
	////7		return new ArrayList<Alt>();
	////7	String[] strs = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8).replace("\r\n", "\u000b").split("\u000b");
	////7	List<Alt> alts = new ArrayList<Alt>();
	////7	for(String s : strs)
	////7		alts.add(csvToAlt(s));
	////7	return alts;
	////7}
    ////7
	////7/**
	////7 * Loads key from the given file.
	////7 * @param file to load key from
	////7 * @return the loaded key
	////7 * @throws IOException
	////7 */
	////7public static SecretKeySpec aesLoadKey(Path file) throws IOException {
	////7	return new SecretKeySpec(Base64.getDecoder().decode(Files.readAllBytes(file)), "AES");
	////7}
    ////7
	////7/**
	////7 * Saves key to the given file.
	////7 * @param key to save
	////7 * @param file to save the key to
	////7 * @throws IOException
	////7 */
	////7public static void aesSaveKey(SecretKeySpec key, Path file) throws IOException {
	////7	if(!file.toFile().exists())
	////7		Files.createFile(file);
	////7	Files.write(file, Base64.getEncoder().encode(key.getEncoded()));
	////7}
    ////7
	////7/**
	////7 * Encrypts and saves the alts using the given key and file.
	////7 * @param key to use in the encryption
	////7 * @param alts to encrypt and save
	////7 * @param file to save to
	////7 * @throws NoSuchAlgorithmException
	////7 * @throws NoSuchPaddingException
	////7 * @throws InvalidKeyException
	////7 * @throws IllegalBlockSizeException
	////7 * @throws BadPaddingException
	////7 * @throws IOException
	////7 */
	////7public static void aesEncrypt(SecretKeySpec key, List<Alt> alts, Path file) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
	////7	if(!file.toFile().exists())
	////7		Files.createFile(file);
	////7	Cipher ci = Cipher.getInstance("AES");
	////7	ci.init(Cipher.ENCRYPT_MODE, key);
	////7	Files.write(file, ci.doFinal(altsToBase64(alts)));
	////7}
    ////7
	////7/**
	////7 * Loads and decrypts the alts using the given file and key.
	////7 * @param key to use in the decryption
	////7 * @param file to load from
	////7 * @return the decrypted alts
	////7 * @throws IllegalBlockSizeException
	////7 * @throws BadPaddingException
	////7 * @throws IOException
	////7 * @throws InvalidKeyException
	////7 * @throws NoSuchAlgorithmException
	////7 * @throws NoSuchPaddingException
	////7 */
	////7public static List<Alt> aesDecrypt(SecretKeySpec key, Path file) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
	////7	Cipher ci = Cipher.getInstance("AES");
	////7	ci.init(Cipher.DECRYPT_MODE, key);
	////7	return base64ToAlts(ci.doFinal(Files.readAllBytes(file)));
	////7}
}