package de.chrissx.mods.movement;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;

public class ACFly2 extends Mod {

	double startY = 0;
	
	public ACFly2() {
		super("Fly-Bypass2");
	}

	@Override
	public void onTick() {
		if(enabled) {
			if(startY > mc.thePlayer.posY)
				mc.thePlayer.motionY = 0.5;
		}
	}
	
	@Override
	public void toggle() {
		enabled = !enabled;
		startY = mc.thePlayer.posY;
	}
}
