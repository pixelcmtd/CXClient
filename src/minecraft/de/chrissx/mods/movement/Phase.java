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
		    mc.thePlayer.fallDistance = 0;
		    mc.thePlayer.onGround = true;
		    if(mc.gameSettings.keyBindJump.isKeyDown())
		    	mc.thePlayer.motionY = 0.1;
		    else if(mc.gameSettings.keyBindSneak.isKeyDown())
		    	mc.thePlayer.motionY = -0.1;
		    else
		    	mc.thePlayer.motionY = 0;
		}
	}
	
	@Override
	public void toggle()
	{
		mc.thePlayer.motionY = 0;
		enabled = !enabled;
	}
}
