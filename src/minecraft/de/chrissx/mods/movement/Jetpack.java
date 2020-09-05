package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Jetpack extends Mod {

	public Jetpack() {
		super("Jetpack", "jetpack");
	}

	@Override
	public void onTick()
	{
		if(enabled && settings().keyBindJump.isKeyDown())
			player().motionY = 0.4;
	}

}
