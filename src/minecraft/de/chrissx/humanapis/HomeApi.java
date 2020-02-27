package de.chrissx.humanapis;

import java.util.ArrayList;

import de.chrissx.util.Random;
import de.chrissx.util.Util;

public class HomeApi {

	public static void sethome(String name)
	{
		Util.sendChat("/sethome " + (name == null ? "" : name));
	}

	private static ArrayList<String> generated_homes = new ArrayList<String>();

	public static String sethome()
	{
		if(generated_homes.size() == 0)
			generated_homes.add("");
		String id = "";
		while(generated_homes.contains(id))
			id = Util.enc64(Random.randBytes(6));
		sethome(id);
		generated_homes.add(id);
		return id;
	}

	public static void home(String name)
	{
		Util.sendChat("/home " + (name == null ? "" : name));
	}
}
