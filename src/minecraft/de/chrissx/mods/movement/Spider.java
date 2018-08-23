package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Spider extends Mod {

	public Spider() {
		super("Spider");
	}

	@Override
	public void onTick()
	{
		if(enabled && mc.thePlayer.isCollidedHorizontally)
			mc.thePlayer.motionY = 0.2;
	}
}
