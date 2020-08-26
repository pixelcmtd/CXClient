package de.chrissx.mods.combat;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class Reach extends Mod {

	float reach = 7;
	File rf;
	
	public Reach() {
		super("Reach");
		rf = getApiFile("reach");
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("range"))
			try {
				reach = Float.parseFloat(args[2]);
			} catch (Exception e) {
				Util.sendMessage("\u00a74Error parsing float.");
			}
		else
			Util.sendMessage("#reach to toggle, #reach range <float> to set your extended range.");
	}
	
	public float getReach() {
		return reach;
	}

	@Override
	public void apiUpdate() {
		write(rf, reach);
	}
}
