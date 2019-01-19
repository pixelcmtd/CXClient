package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;

public class Fasthit extends Mod {

	public Fasthit() {
		super("FastHit");
	}

	@Override
	public void onTick() {
		if(enabled)
			mc.leftClickCounter = 0;
	}
}