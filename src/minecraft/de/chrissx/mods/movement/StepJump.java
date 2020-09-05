package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class StepJump extends Mod {

	public StepJump() {
		super("StepJump", "stepjump");
	}

	@Override
	public void onTick() {
		if(player().isCollidedHorizontally && player().onGround) {
			player().jump();
			player().motionY = 1;
		}
	}
}
