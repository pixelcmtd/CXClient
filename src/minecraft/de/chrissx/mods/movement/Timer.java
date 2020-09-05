package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class Timer extends Mod {

	float factor = 2;
	File ff;
	
	public Timer() {
		super("Timer", "timer");
		ff = getApiFile("factor");
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("speed"))
			try {
				factor = Float.parseFloat(args[2]);
				net.minecraft.util.Timer.timerSpeed = enabled ? factor : 1;
			} catch (Exception e) {
				Util.sendMessage("\u00a74Error parsing float.");
			}
		else
			Util.sendMessage("#timer to toggle, #timer speed <double> to change the factor.");
	}
	
	@Override
	public void toggle() {
		enabled = !enabled;
		net.minecraft.util.Timer.timerSpeed = enabled ? factor : 1;
	}

	@Override
	public void apiUpdate() {
		write(ff, factor);
	}
}
