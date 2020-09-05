package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Spider extends Mod {

	public Spider() {
		super("Spider", "spider");
	}

	@Override
	public void onTick()
	{
		if(enabled && player().isCollidedHorizontally)
			player().motionY = 0.2;
	}
}
