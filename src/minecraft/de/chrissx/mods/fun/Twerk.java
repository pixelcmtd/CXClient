package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;

public class Twerk extends Mod {

	public Twerk() {
		super("Twerk");
	}

	@Override
	public void onTick() {
		if(enabled)
			mc.thePlayer.setSneaking(!mc.thePlayer.isSneaking());
	}
}