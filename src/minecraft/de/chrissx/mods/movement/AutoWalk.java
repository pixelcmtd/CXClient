package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class AutoWalk extends Mod {

	public AutoWalk() {
		super("AutoWalk");
	}

	@Override
	public void onTick()
	{
		if(enabled)
			mc.gameSettings.keyBindForward.pressed = true;
	}
}
