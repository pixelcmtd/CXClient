package de.chrissx.mods.fun;

import de.chrissx.mods.Semimod;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;

public class Text extends Semimod {

	public Text() {
		super("Text");
	}

	@Override
	public void onHotkey() {
		processCommand(new String[] {"#text", Consts.clientName + " by chrissx, your server is 2eZ!"});
	}

	@Override
	public void processCommand(String[] args) {
		String message = "";
        for (int i = 1; i < args.length; i++)
        	message += args[i] + " ";
        Util.cheatArmorStand(message.replace('&', '\u00a7'), player().posX, player().posY, player().posZ, 36);
	}

	@Override
	public void toggle() {}
}