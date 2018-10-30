package de.chrissx.alts;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltManager {

	public static final String DEFAULT_USER = "CXCLIENT_DEFAULT", DEFAULT_PASS = "MINECRAFT_LAUNCHER";
	public Alt currentAlt = new Alt(DEFAULT_USER, DEFAULT_PASS);
	YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new
			YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
	Minecraft mc = Minecraft.getMinecraft();
	List<Alt> alts;
	public static final File ALT_FILE = Paths.get(Consts.configPath, "altmanageralts.crypt").toFile();
	public static final Path KEY_FILE = Paths.get(Consts.configPath, "altmanagerkey.crypt");
	SecretKeySpec key;
	final String keyString = "CXCLIENT___THE CAKE'S A LIE--DO NOT TRY TO CRACK THIS*�#+���+*'.:;_:-.,.-.-,()()8145689147";

	public AltManager() {
		if(KEY_FILE.toFile().exists())
			try {
				key = AltCryptography.aesLoadKey(KEY_FILE);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					key = AltCryptography.createKey(Util.randomSortString(keyString));
				} catch (Exception e2) {
					e2.printStackTrace();
					key = new SecretKeySpec(new byte[] {1,0,0,1,0,0,1,1,1,1,0,0,0,0,1,1}, "AES");
				}
			}
		else
			try {
				key = AltCryptography.createKey(Util.randomSortString(keyString));
			} catch (Exception e) {
				e.printStackTrace();
				key = new SecretKeySpec(new byte[] {1,0,0,1,0,0,1,1,1,1,0,0,0,0,1,1}, "AES");
			}
		
		if(ALT_FILE.exists())
			try {
				alts = AltCryptography.aesDecrypt(key, ALT_FILE.toPath());
			} catch (Exception e) {
				e.printStackTrace();
				alts = new ArrayList<Alt>();
			}
		else
			alts = new ArrayList<Alt>();
	}
	
	public void onShutdown() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		AltCryptography.aesSaveKey(key, KEY_FILE);
		AltCryptography.aesEncrypt(key, alts, ALT_FILE.toPath());
	}
	
	public List<Alt> getAlts() {
		return alts;
	}
	
	public void login(String name, String pass) throws AuthenticationException {
		currentAlt = new Alt(name, pass);
		patchAlt(currentAlt);
		alts.add(currentAlt);
	}
	
	void patchAlt(Alt a) throws AuthenticationException {
		if(auth.isLoggedIn())
			auth.logOut();
		if(!a.isCracked()) {
			auth.setUsername(a.getEmail());
			auth.setPassword(a.getPassword());
			auth.logIn();
			mc.session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
		}else
			mc.session = new Session(a.getEmail(), "", "", "mojang");
	}
	
	Alt getAlt(String name) throws AuthenticationException, AltNotFoundException {
		for(Alt a : alts) {
			if(getName(a).equalsIgnoreCase(name))
				return a;
		}
		throw new AltNotFoundException("Can't get alt from name.");
	}
	
	public String getName(Alt a) throws AuthenticationException {
		return getName(a.getEmail(), a.getPassword());
	}
	
	public String getName(String email, String pass) throws AuthenticationException {
		if(auth.isLoggedIn())
			auth.logOut();
		auth.setUsername(email);
		auth.setPassword(pass);
		auth.logIn();
		return auth.getSelectedProfile().getName();
	}
	
	public void loadAlt(String name) throws AuthenticationException, AltNotFoundException {
		patchAlt(getAlt(name));
	}
}