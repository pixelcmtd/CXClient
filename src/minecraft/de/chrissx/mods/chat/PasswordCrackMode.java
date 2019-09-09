package de.chrissx.mods.chat;

public enum PasswordCrackMode {

	DICT(0),
	BRUTE_FORCE(1);

	public final int id;

	PasswordCrackMode(int id)
	{
		this.id = id;
	}

	public static PasswordCrackMode parse(String s) throws NumberFormatException, Exception
	{
		if(s.toUpperCase().startsWith("DICT"))
			return DICT;
		else if(s.toUpperCase().startsWith("BRUTE"))
			return BRUTE_FORCE;
		else
			return match(Integer.parseInt(s));
	}

	public static PasswordCrackMode match(int id) throws Exception
	{
		if(id == 0) return DICT;
		else if(id == 1) return BRUTE_FORCE;
		else throw new Exception("Can't match id " + id + " to a PasswordCrackMode.");
	}
}
