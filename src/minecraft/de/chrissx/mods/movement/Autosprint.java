package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

// TODO: merge this, AutoWalk, AutoJump, legit Sneak, Dolphin, ... into a key down system
public class Autosprint extends Mod {

	public Autosprint() {
		super("Sprint", "sprint", "Makes you sprint, as if you held down control");
	}

	@Override
	public void onTick() {
		settings().keyBindSprint.pressed = true;
	}
}
