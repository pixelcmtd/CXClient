package de.chrissx.mods.building;

import de.chrissx.mods.Mod;

public class FastPlace extends Mod {

	public FastPlace() {
		super("FastPlace", "fastplace");
	}

	@Override
	public void onTick() {
		mc.rightClickDelayTimer = 0;
	}
}