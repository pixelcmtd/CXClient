package de.chrissx.mods.movement;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;

public class Autosprint extends Mod {

	public Autosprint() {
		super("Sprint");
	}

	@Override
	public void onTick() {
		if(enabled)
			if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isEating() && !mc.thePlayer.isSprinting())
				mc.thePlayer.setSprinting(true);
			else if(mc.thePlayer.isSprinting())
				mc.thePlayer.setSprinting(false);
	}
}
