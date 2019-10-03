package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Phase extends Mod {

	public Phase() {
		super("Phase");
	}

	@Override
	public void onTick()
	{
		if(enabled)
		{
		    player().fallDistance = 0;
		    player().onGround = true;
		    if(mc.gameSettings.keyBindJump.isKeyDown())
		    	player().motionY = 0.1;
		    else if(mc.gameSettings.keyBindSneak.isKeyDown())
		    	player().motionY = -0.1;
		    else
		    	player().motionY = 0;
		}
	}

	@Override
	public void toggle()
	{
		player().motionY = 0;
		enabled = !enabled;
	}
}
