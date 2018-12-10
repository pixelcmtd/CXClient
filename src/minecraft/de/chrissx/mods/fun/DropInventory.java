package de.chrissx.mods.fun;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import net.minecraft.client.Minecraft;

public class DropInventory implements Bindable, Commandable {

	@Override
	public void processCommand(String[] args) {
		Minecraft.getMinecraft().thePlayer.inventory.dropAllItems();
	}

	@Override
	public void onHotkey() {
		Minecraft.getMinecraft().thePlayer.inventory.dropAllItems();
	}

	@Override
	public String getName() {
		return "DropInventory";
	}

}
