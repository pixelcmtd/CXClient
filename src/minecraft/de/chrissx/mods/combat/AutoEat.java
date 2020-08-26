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
		if(enabled && player().canEat(false) &&
				(i = Util.firstFoodIndex(inventory().mainInventory)) != -1)
		{
			inventory().currentItem = i - 27;
			//TODO: START EATING
		}
	}
}
