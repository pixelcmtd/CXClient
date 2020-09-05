package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class AutoEat extends Mod {

	public AutoEat() {
		super("AutoEat", "autoeat");
	}

	@Override
	public void onTick()
	{
		int i = Util.firstFoodIndex(inventory().mainInventory);
		if(player().canEat(false) && i != -1)
		{
			inventory().currentItem = i - 27;
			//TODO: START EATING
		}
	}
}
