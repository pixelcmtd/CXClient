package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Dolphin extends Mod {

	public Dolphin() {
		super("Dolphin", "dolphin", "Makes you swim, as if you held down space");
	}

	@Override
	public void onTick() {
		if (player().isInWater())
			player().motionY += 0.04;
	}
}
