package de.chrissx.mods.fun;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagList;

public class TrollPotion implements Bindable, Commandable {

	@Override
	public void onHotkey() {
		if(!Minecraft.getMinecraft().playerController.isInCreativeMode()) {
			Util.sendMessage("\u00a74You have to be in gm 1 in order to execute this!");
			return;
		}
		NBTTagList l = Util.newEffects();
		for(int i = 0; i < 23; i++)
			l = Util.addEffect(l, i, Integer.MAX_VALUE, Integer.MAX_VALUE);
		Util.cheatItem(Util.getCustomPotion(l, "Troller Potion of Trolling"), 36);
	}

	@Override
	public void processCommand(String[] args) {
		onHotkey();
	}

	@Override
	public String getName() {
		return "TrollPotion";
	}
}