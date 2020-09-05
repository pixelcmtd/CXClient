package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Autosprint extends Mod {

	public Autosprint() {
		super("Sprint", "sprint");
	}

	@Override
	public void onTick() {
		settings().keyBindSprint.pressed = true;
	}
}
