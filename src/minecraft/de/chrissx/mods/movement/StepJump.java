package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class StepJump extends Mod {

	public StepJump() {
		super("StepJump");
	}

	@Override
	public void onTick() {
		if(enabled)
			if(player().isCollidedHorizontally && player().onGround) {
				player().jump();
				player().motionY = 1;
			}
	}
}
