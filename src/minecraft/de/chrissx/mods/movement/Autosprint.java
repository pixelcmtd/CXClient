package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Autosprint extends Mod {

	public Autosprint() {
		super("Sprint");
	}

	@Override
	public void onTick() {
		if(enabled)
			settings().keyBindSprint.pressed = true;
	}
}
