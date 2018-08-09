package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class FastLadder extends Mod {

	double motion = 0.2872;
	File mf;
	
	public FastLadder() {
		super("FastLadder");
		mf = getApiFile("motion");
	}

	@Override
	public void onTick() {
		if(enabled && mc.thePlayer.isOnLadder())
			mc.thePlayer.motionY = motion;
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("motion"))
			try {
				motion = Double.parseDouble(args[2]);
			} catch (Exception e) {
				Util.sendMessage("Error parsing double.");
			}
		else
			Util.sendMessage("#fastladder to toggle, #fastladder <double> to set motion(pushing w: 0.1437, default fastladder: 0.2872).");
	}

	@Override
	public void apiUpdate() {
		write(mf, motion);
	}
}