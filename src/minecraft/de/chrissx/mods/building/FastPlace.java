package de.chrissx.mods.building;

import de.chrissx.mods.Mod;

public class FastPlace extends Mod {

	public FastPlace() {
		super("FastPlace", "fastplace", "Allows you to place blocks as fast as you can click");
	}

	@Override
	public void onTick() {
		mc.rightClickDelayTimer = 0;
	}
}