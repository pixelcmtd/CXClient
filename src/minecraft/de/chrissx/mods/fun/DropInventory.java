package de.chrissx.mods.fun;

import de.chrissx.mods.Semimod;

public class DropInventory extends Semimod {

	public DropInventory() {
		super("DropInventory", "dropinventory", "Drops your whole inventory");
	}

	@Override
	public void toggle() {
		inventory().dropAllItems();
	}
}
