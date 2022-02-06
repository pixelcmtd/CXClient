package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;

public class Fasthit extends Mod {

	public Fasthit() {
		super("FastHit", "Allows you to hit as fast as you can click");
	}

	@Override
	public void onTick() {
		mc.leftClickCounter = 0;
	}
}