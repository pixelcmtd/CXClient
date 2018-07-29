package de.chrissx.mods.fun;

import de.chrissx.HackedClient;
import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;

public class Text implements Bindable, Commandable {

	private Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void onHotkey() {
		processCommand(new String[] {"#text", HackedClient.getClient().CLIENT_NAME + " by chrissx, your server is 2eZ!"});
	}

	@Override
	public void processCommand(String[] args) {
		String message = "";
        for (int i = 1; i < args.length; i++) {
        	message = message + args[i] + " ";
        }
        Util.cheatArmorStand(message.replace('&', '§'), mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, 36);
	}

	@Override
	public String getName() {
		return "Text";
	}
}