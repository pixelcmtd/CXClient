package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class LegitSpeed extends Mod {

	public LegitSpeed() {
		super("LegitSpeed");
	}

	@Override
	public void onTick() {
		if(enabled && mc.thePlayer.onGround) {
			mc.thePlayer.jump();
			mc.thePlayer.setSprinting(true);
		}
	}
}
