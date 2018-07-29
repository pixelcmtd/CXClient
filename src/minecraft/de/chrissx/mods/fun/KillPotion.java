package de.chrissx.mods.fun;

import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class KillPotion implements Commandable, Bindable {

	@Override
	public void processCommand(String[] args) {
		Util.cheatItem(Util.getCustomPotion(Util.getEffect(6, 125, 2000), "Killer Potion of Death"), 36);
	}

	@Override
	public void onHotkey() {
		Util.cheatItem(Util.getCustomPotion(Util.getEffect(6, 125, 2000), "Killer Potion of Death"), 36);
	}

	@Override
	public String getName() {
		return "KillPotion";
	}
}