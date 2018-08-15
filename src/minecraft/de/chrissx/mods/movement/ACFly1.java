package de.chrissx.mods.movement;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;

public class ACFly1 extends Mod {

	public ACFly1() {
		super("Fly-Bypass1");
	}

	@Override
	public void onTick() {
		if(enabled)
			if(mc.thePlayer.isSneaking())
				mc.thePlayer.motionY = -0.4;
			else if(mc.gameSettings.keyBindJump.isKeyDown())
				mc.thePlayer.motionY = 0.4;
			else
				mc.thePlayer.motionY = 0;
	}
}
