package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Dolphin extends Mod {

	public Dolphin() {
		super("Dolphin");
	}

	@Override
	public void onTick()
	{
		if (enabled && mc.thePlayer.isInWater())
			mc.thePlayer.motionY += 0.04;
	}
}
