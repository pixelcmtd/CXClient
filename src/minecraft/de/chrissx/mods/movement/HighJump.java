package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class HighJump extends Mod {

	public double motion = 0.75;

	public HighJump() {
		super("HighJump", "highjump", "Allows you to jump higher");
	}

	@Override
	public void processCommand(String[] args) {
		if (args.length == 1)
			toggle();
		else if (args[1].equalsIgnoreCase("height"))
			try {
				double d = Double.parseDouble(args[2]);
				if (d <= 1)
					motion = 0.42 * d;
				else {
					motion = 0.42;
					d -= 1;
					if (d <= 1)
						motion += 0.15 * d;
					else {
						motion += 0.15;
						d -= 1;
						if (d <= 9)
							motion += d / 10;
						else {
							motion += 0.9;
							d -= 9;
							motion += d * 0.07;
						}
					}
				}
			} catch (Exception e) {
				Util.sendMessage("\u00a74Error parsing height: " + e.getMessage());
			}
		else
			Util.sendMessage("#highjump to toggle, #highjump height <double> to set jump height");
	}
}
