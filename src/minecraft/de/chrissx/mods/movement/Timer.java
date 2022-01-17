package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.FloatOption;

public class Timer extends Mod {

	FloatOption factor = new FloatOption("factor", "The factor", 2);

	public Timer() {
		super("Timer", "timer", "Speeds up everything");
		addOption(factor);
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		net.minecraft.util.Timer.timerSpeed = enabled ? factor.value : 1;
	}

	@Override
	public void onTick() {
		net.minecraft.util.Timer.timerSpeed = factor.value;
	}
}
