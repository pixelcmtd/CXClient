package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

// TODO: why does this exist when Glide does also
public class ACFly1 extends Mod {

	public ACFly1() {
		super("Fly-Bypass1", "Makes you fly by just not falling down, like Glide");
		setArgv0("flyac1");
	}

	@Override
	public void onTick() {
		if (player().isSneaking())
			player().motionY = -0.4;
		else if (settings().keyBindJump.isKeyDown())
			player().motionY = 0.4;
		else
			player().motionY = 0;
		player().motionX *= 1.1;
		player().motionZ *= 1.1;
	}
}
