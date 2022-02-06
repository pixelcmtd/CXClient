package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.item.ItemSoup;

public class AutoSoup extends Mod {
	// TODO: would a variable min be a good idea?

	public AutoSoup() {
		super("AutoSoup", "Automatically soups when your health is 6 hearts or less");
	}

	@Override
	public void onTick() {
		int i = Util.firstHotbarIndex(ItemSoup.class, inventory().mainInventory);
		if (player().getHealth() < (player().getMaxHealth() - 7) && i != -1) {
			int before = inventory().currentItem;
			inventory().currentItem = i;
			click(false);
			inventory().currentItem = before;
		}
	}
}
