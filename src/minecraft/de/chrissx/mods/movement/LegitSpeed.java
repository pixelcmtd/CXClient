package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class LegitSpeed extends Mod {

	public LegitSpeed() {
		super("LegitSpeed");
	}

	@Override
	public void onTick() {
		if(enabled && player().onGround) {
			player().jump();
			player().setSprinting(true);
		}
	}
}
