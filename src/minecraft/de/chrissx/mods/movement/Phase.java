package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Phase extends Mod {

	public Phase() {
		super("Phase", "A weird style of movement that exploits an ancient NoCheatPlus bug to pass through blocks");
	}

	@Override
	public void onTick() {
		player().fallDistance = 0;
		player().onGround = true;
		if (settings().keyBindJump.isKeyDown())
			player().motionY = 0.1;
		else if (settings().keyBindSneak.isKeyDown())
			player().motionY = -0.1;
		else
			player().motionY = 0;
	}

	@Override
	public void toggle() {
		player().motionY = 0;
		enabled = !enabled;
	}
}
