package de.chrissx.mods.fun;

import org.apache.http.client.utils.URIBuilder;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class KillPotion implements Commandable, Bindable {

	@Override
	public void processCommand(String[] args) {
		Util.cheatItem(Util.getCustomPotion(Util.addEffect(Util.newEffects(), 6, 125, 2000), "Killer Potion of Death"), 36);
	}

	@Override
	public void onHotkey() {
		processCommand(null);
	}

	@Override
	public String getName() {
		return "KillPotion";
	}
}