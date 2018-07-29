package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class StepJump extends Mod {

	public StepJump() {
		super("StepJump");
	}

	@Override
	public void onTick() {
		if(enabled)
			if(mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
				mc.thePlayer.jump();
				mc.thePlayer.motionY = 1;
			}
	}
}
