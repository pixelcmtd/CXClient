package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.DoubleOption;

public class FastFall extends Mod {

	// TODO: try what happens when this is positive
	DoubleOption speed = new DoubleOption("speed", "The factor", -4);
	File sf;

	public FastFall() {
		super("FastFall", "fastfall", "Makes you fall faster, or slower, or up?");
		addOption(speed);
		sf = getApiFile("speed");
	}

	@Override
	public void onTick() {
		if (!player().onGround && player().motionY < 0)
			player().motionY = speed.value;
	}

	@Override
	public void apiUpdate() {
		write(sf, speed.value);
	}
}
