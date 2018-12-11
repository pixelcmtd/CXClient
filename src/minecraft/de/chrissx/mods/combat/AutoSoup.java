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
		if(enabled && mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() &&
				(i = Util.firstSoupIndex(
						mc.thePlayer.inventory.mainInventory)) != -1)
		{
			mc.thePlayer.inventory.currentItem = i;
			mc.rightClickMouse();
		}
	}
}
