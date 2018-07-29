package de.chrissx.mods.fun;

import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;

public class TrollPotion implements Bindable, Commandable {

	@Override
	public void onHotkey() {
		if(!Minecraft.getMinecraft().playerController.isInCreativeMode()) {
			Util.sendMessage("§4You have to be in gm 1 in order to execute this!");
			return;
		}
		NBTTagList l = Util.getEffect(0, Integer.MAX_VALUE, Integer.MAX_VALUE);
		for(int i = 1; i < 23; i++)
			l = Util.addEffect(l, i, Integer.MAX_VALUE, Integer.MAX_VALUE);
		Util.cheatItem(Util.getCustomPotion(l, "Troller Potion of Trolling"), 36);
	}

	@Override
	public void processCommand(String[] args) {
		if(!Minecraft.getMinecraft().playerController.isInCreativeMode()) {
			Util.sendMessage("§4You have to be in gm 1 in order to execute this!");
			return;
		}
		NBTTagList l = Util.getEffect(0, Integer.MAX_VALUE, Integer.MAX_VALUE);
		for(int i = 1; i < 23; i++)
			l = Util.addEffect(l, i, Integer.MAX_VALUE, Integer.MAX_VALUE);
		Util.cheatItem(Util.getCustomPotion(l, "Troller Potion of Trolling"), 36);
	}

	@Override
	public String getName() {
		return "TrollPotion";
	}

}