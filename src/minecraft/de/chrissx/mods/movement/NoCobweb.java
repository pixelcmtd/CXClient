package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class NoCobweb extends Mod {

	public NoCobweb() {
		super("NoCobweb", "nocobweb");
	}

	@Override
	public void onTick()
	{
		if(enabled)
			player().isInWeb = false;
	}
}
