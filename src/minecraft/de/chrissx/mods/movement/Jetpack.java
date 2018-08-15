package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Jetpack extends Mod {

	public Jetpack() {
		super("Jetpack");
	}
	
	@Override
	public void onTick()
	{
		if(enabled && mc.gameSettings.keyBindJump.isKeyDown())
			mc.thePlayer.motionY = 0.4;
	}

}
