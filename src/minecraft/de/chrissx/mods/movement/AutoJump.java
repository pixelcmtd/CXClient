package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class AutoJump extends Mod {

	public AutoJump() {
		super("AutoJump", "autojump", "Automatically jumps whenever you are on the ground");
	}

	@Override
	public void onTick() {
		if (player().onGround)
			player().jump();
	}

}
