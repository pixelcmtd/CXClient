package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.item.ItemFood;

public class AutoEat extends Mod {

	public AutoEat() {
		super("AutoEat", "Switches to the first slot with food when you have hunger");
	}

	@Override
	public void onTick() {
		int i = Util.firstHotbarIndex(ItemFood.class, inventory().mainInventory);
		if (player().canEat(false) && i != -1) {
			inventory().currentItem = i - 27;
			// TODO: START EATING
		}
	}
}
