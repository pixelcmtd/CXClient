package de.chrissx.mods.fun;

import de.chrissx.mods.Semimod;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;

public class Text extends Semimod {

	String[] last = new String[] { "#text", Consts.clientName + ", because your server is 2eZ!" };

	public Text() {
		super("Text", "text", "Gives you an armor stand that will make a given text appear when placed");
	}

	@Override
	public void onHotkey() {
		processCommand(last);
	}

	@Override
	public void processCommand(String[] args) {
		String message = "";
		for (int i = 1; i < args.length; i++)
			message += args[i] + " ";
		Util.cheatArmorStand(message.replace('&', '\u00a7'), player().posX, player().posY, player().posZ, 36);
		last = args;
	}

	@Override
	public void toggle() {
	}
}