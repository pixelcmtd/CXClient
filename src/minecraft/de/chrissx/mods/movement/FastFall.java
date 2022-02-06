package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.DoubleOption;

public class FastFall extends Mod {

	// TODO: try what happens when this is positive
	DoubleOption speed = new DoubleOption("speed", "The factor", -4);

	public FastFall() {
		super("FastFall", "Makes you fall faster, or slower, or up?");
		addOption(speed);
	}

	@Override
	public void onTick() {
		if (!player().onGround && player().motionY < 0)
			player().motionY = speed.value;
	}
}
