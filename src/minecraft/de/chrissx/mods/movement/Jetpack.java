package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Jetpack extends Mod {

	public Jetpack() {
		// TODO: speed option
		super("Jetpack", "Allows you to fly up by holding down the jump key");
	}

	@Override
	public void onTick() {
		if (settings().keyBindJump.isKeyDown())
			player().motionY = 0.4;
	}

}
