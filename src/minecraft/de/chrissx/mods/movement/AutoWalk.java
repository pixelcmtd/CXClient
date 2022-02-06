package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class AutoWalk extends Mod {

	public AutoWalk() {
		super("AutoWalk", "Makes you walk forward, as if you held down W");
	}

	@Override
	public void onTick() {
		settings().keyBindForward.pressed = true;
	}
}
