package de.chrissx.mods.movement;

import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;

public class VanillaFly extends Mod {

	public VanillaFly() {
		super("VanillaFly");
	}
	
	public void setSpeed(float speed) {
		mc.thePlayer.capabilities.setFlySpeed(speed);
	}
	
	@Override
	public void toggle() {
		enabled = !enabled;
		try {
			if(enabled)
				mc.thePlayer.capabilities.allowFlying = true;
			else
				mc.thePlayer.capabilities.allowFlying = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("speed"))
			try {
				setSpeed(Float.parseFloat(args[2]));
			} catch (Exception e) {
				Util.sendMessage("§4Error parsing float.");
			}
		else
			Util.sendMessage("#fly to toggle, #fly speed <float> to set speed.");
	}
}