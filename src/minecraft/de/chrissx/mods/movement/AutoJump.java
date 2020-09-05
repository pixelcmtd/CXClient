package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class AutoJump extends Mod {

	public AutoJump() {
		super("AutoJump", "autojump");
	}

	@Override
	public void onTick()
	{
		if(player().onGround)
			player().jump();
	}

}
