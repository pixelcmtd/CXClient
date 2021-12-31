package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class NoCobweb extends Mod {

	public NoCobweb() {
		super("NoCobweb", "nocobweb", "Stops cobwebs from slowing you down");
	}

	@Override
	public void onTick() {
		player().isInWeb = false;
	}
}
