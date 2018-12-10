package de.chrissx.alts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
	
	public void storeVault(String file, String password) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
	{
		ByteArrayOutputStream raw = new ByteArrayOutputStream();
		for(Alt a : alts)
		{
			raw.write(a.getEmail().getBytes(StandardCharsets.UTF_8));
			raw.write(10);
			raw.write(a.getPassword().getBytes(StandardCharsets.UTF_8));
			raw.write(10);
		}
		AltCryptography.encrypt(Paths.get(file), password, raw.toByteArray());
	}
	
	public void loadVault(String file, String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException
	{
		ByteArrayInputStream raw = new ByteArrayInputStream(
				AltCryptography.decrypt(Paths.get(file), password));
		alts = new ArrayList<Alt>();
		int i;
		while((i = raw.read()) != -1)
		{
			List<Byte> bfr = new ArrayList<Byte>();
			while(i != 10)
			{
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
	 * Loads the alts from the given CXColonSeparatedValues-file.
	 * @param cxcsv The path of the file the CXCSV is located in.
	 * @return The loaded Alts.
	 */
	public void loadCxcsv(Path file) throws IOException
	{
		for(String s : Files.readAllLines(file))
		{
			String[] t = s.split(":");
			alts.add(new Alt(t[0], t[1]));
		}
	}
}