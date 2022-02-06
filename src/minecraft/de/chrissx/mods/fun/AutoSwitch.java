package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;

public class AutoSwitch extends Mod {

	public AutoSwitch() {
		super("AutoSwitch", "Automatically cycles through your hotbar");
	}

	public void onTick() {
		if (inventory().currentItem == 8)
			inventory().currentItem = 0;
		else
			inventory().currentItem++;
	}
}
