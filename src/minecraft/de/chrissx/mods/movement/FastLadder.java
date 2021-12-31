package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.DoubleOption;

public class FastLadder extends Mod {

	DoubleOption motion = new DoubleOption("motion", "The speed, 0.1437 in Vanilla", 0.2872);
	File mf;

	public FastLadder() {
		super("FastLadder", "fastladder", "Speeds up climbing ladders");
		addOption(motion);
		mf = getApiFile("motion");
	}

	@Override
	public void onTick() {
		if (player().isOnLadder())
			player().motionY = motion.value;
	}

	@Override
	public void apiUpdate() {
		write(mf, motion.value);
	}
}