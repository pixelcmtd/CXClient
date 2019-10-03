package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class AutoSoup extends Mod {

	public AutoSoup() {
		super("AutoSoup");
	}

	@Override
	public void onTick()
	{
		int i;
		if(enabled && player().getHealth() < player().getMaxHealth() &&
			(i = Util.firstSoupIndex(inventory().mainInventory)) != -1)
		{
			player().inventory.currentItem = i;
			mc.rightClickMouse();
		}
	}
}
