package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class StepJump extends Mod {

	public StepJump() {
		super("StepJump", "stepjump", "Makes you jump to step up a block?");
	}

	// FIXME: this is hopelessly broken
	@Override
	public void onTick() {
		if(player().isCollidedHorizontally && player().onGround) {
			player().jump();
			player().motionY = 1;
		}
	}
}
