package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;

public class AutoSwitch extends Mod {

	public AutoSwitch() {
		super("AutoSwitch");
	}

	public void onTick()
	{
		if(enabled)
			if(mc.thePlayer.inventory.currentItem == 8)
				mc.thePlayer.inventory.currentItem = 0;
			else
				mc.thePlayer.inventory.currentItem++;
	}
}
