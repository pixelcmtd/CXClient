package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Dolphin extends Mod {

	public Dolphin() {
		super("Dolphin", "dolphin");
	}

	@Override
	public void onTick()
	{
		if (enabled && player().isInWater())
			player().motionY += 0.04;
	}
}
