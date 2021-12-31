package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class VanillaFly extends Mod {

	public VanillaFly() {
		super("VanillaFly", "flyvanilla", "Makes you fly like in creative mode");
	}

	public void setSpeed(float speed) {
		player().capabilities.setFlySpeed(speed);
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		try {
			// TODO: fix this for creative mode
			player().capabilities.allowFlying = enabled;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processCommand(String[] args) {
		if (args.length == 1)
			toggle();
		else if (args[1].equalsIgnoreCase("speed"))
			try {
				setSpeed(Float.parseFloat(args[2]));
			} catch (Exception e) {
				Util.sendMessage("\u00a74Error parsing float.");
			}
		else
			Util.sendMessage("#fly to toggle, #fly speed <float> to set speed.");
	}
}