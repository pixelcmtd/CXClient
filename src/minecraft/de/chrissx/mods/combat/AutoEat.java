package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class AutoEat extends Mod {

	public AutoEat() {
		super("AutoEat");
	}

	@Override
	public void onTick()
	{
		int i;
		if(mc.thePlayer.canEat(false) &&
				(i = Util.firstFoodIndex(
						mc.thePlayer.inventory.mainInventory))
				!= -1)
		{
			mc.thePlayer.inventory.currentItem = i - 27;
			//START EATING
		}
	}
}
