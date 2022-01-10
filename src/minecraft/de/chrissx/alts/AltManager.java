package de.chrissx.alts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltManager {

	public Alt currentAlt = new Alt("LOGGED IN WITH", "THE MINECRAFT LAUNCHER");
	YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new
	                                   YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
	Minecraft mc = Minecraft.getMinecraft();
	List<Alt> alts;

	public AltManager() {
		alts = new ArrayList<Alt>();
	}

	public List<Alt> getAlts() {
		return alts;
	}

	/**
	 * alts.clear()
	 */
	public void clear() {
		alts.clear();
	}

	/**
	 * adds a new alt { name, pass } and runs patchAlt() on it
	 * @param name the name/email for the alt
	 * @param pass the password for the alt
	 * @throws AuthenticationException
	 */
	public void login(String name, String pass) throws AuthenticationException {
		currentAlt = new Alt(name, pass);
		patchAlt(currentAlt);
		alts.add(currentAlt);
	}

	/**
	 * logs into the alt and sets mc.session accordingly
	 * @param a the alt
	 * @throws AuthenticationException
	 */
	void patchAlt(Alt a) throws AuthenticationException {
		if(auth.isLoggedIn())
			auth.logOut();
		if(!a.isCracked()) {
			auth.setUsername(a.getEmail());
			auth.setPassword(a.getPassword());
			auth.logIn();
			mc.session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
		} else
			mc.session = new Session(a.getEmail(), "", "", "mojang");
	}

	/**
	 * gets an alt from ingame name
	 * @param name the name of the alt
	 * @return the alt
	 * @throws AuthenticationException
	 * @throws AltNotFoundException
	 */
	Alt getAlt(String name) throws AuthenticationException, AltNotFoundException {
		for(Alt a : alts)
			if(getName(a).equalsIgnoreCase(name))
				return a;
		throw new AltNotFoundException("Can't get alt from name.");
	}

	/**
	 * gets the ingame name from the alt
	 * @param a the alt
	 * @return the ig name
	 * @throws AuthenticationException
	 */
	public String getName(Alt a) throws AuthenticationException {
		return a.isCracked() ? a.getEmail() : getName(a.getEmail(), a.getPassword());
	}

	/**
	 * gets the name of the minecraft account with email and pass
	 * @param email the email of the account
	 * @param pass the password of the account
	 * @return the name of the account
	 * @throws AuthenticationException
	 */
	public String getName(String email, String pass) throws AuthenticationException {
		if(auth.isLoggedIn())
			auth.logOut();
		auth.setUsername(email);
		auth.setPassword(pass);
		auth.logIn();
		return auth.getSelectedProfile().getName();
	}

	/**
	 * tries to load the alt found by name
	 * @param name
	 * @throws AuthenticationException
	 * @throws AltNotFoundException
	 */
	public void loadAlt(String name) throws AuthenticationException, AltNotFoundException {
		patchAlt(getAlt(name));
	}

	/**
	 * Uses password to store the alts a new Vault at file.
	 * @param file the file to save to
	 * @param password the password to use
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public void storeVault(String file, String password) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		ByteArrayOutputStream raw = new ByteArrayOutputStream();
		for(Alt a : alts) {
			raw.write(a.getEmail().getBytes(StandardCharsets.UTF_8));
			raw.write(10);
			raw.write(a.getPassword().getBytes(StandardCharsets.UTF_8));
			raw.write(10);
		}
		AltCryptography.encrypt(Paths.get(file), password, raw.toByteArray());
	}

	/**
	 * Uses password to load the Vault at file.
	 * @param file the file to load from
	 * @param password the password used to decrypt
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 */
	public void loadVault(String file, String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		ByteArrayInputStream raw = new ByteArrayInputStream(
		    AltCryptography.decrypt(Paths.get(file), password));
		alts = new ArrayList<Alt>();
		int i;
		while((i = raw.read()) != -1) {
			List<Byte> bfr = new ArrayList<Byte>();
			while(i != 10) {
				bfr.add((byte)i);
				i = raw.read();
			}
			byte[] rs = new byte[bfr.size()];
			for(i = 0; i < bfr.size(); i++)
				rs[i] = bfr.get(i);
			String email = new String(rs, StandardCharsets.UTF_8);
			bfr.clear();
			while((i = raw.read()) != 10)
				bfr.add((byte)i);
			rs = new byte[bfr.size()];
			for(i = 0; i < bfr.size(); i++)
				rs[i] = bfr.get(i);
			alts.add(new Alt(email, new String(rs, StandardCharsets.UTF_8)));
		}
	}

	/**
	 * Loads the alts from the given CXCSV-file.
	 * @param file The path of the file the CXCSV is located in.
	 * @return The loaded Alts.
	 */
	public void loadCxcsv(Path file) throws Exception {
		for(String s : Files.readAllLines(file)) {
			try {
				String[] t = s.split(":");
				alts.add(new Alt(t[0], t[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves all alts to a CXCSV at file.
	 * @param file the file to save to
	 * @throws IOException
	 */
	public void saveCxcsv(String file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		for(Alt a : alts) {
			byte[] b = a.getEmail().getBytes();
			fos.write(b, 0, b.length);
			fos.write(':');
			b = a.getPassword().getBytes();
			fos.write(b, 0, b.length);
		}
		fos.close();
	}
}