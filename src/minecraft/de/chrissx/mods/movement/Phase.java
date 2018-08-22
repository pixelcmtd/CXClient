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
			mc.thePlayer.noClip = true;
		    //mc.thePlayer.fallDistance = 0;
		    mc.thePlayer.onGround = false;
		    mc.thePlayer.motionY = 0;
		    mc.thePlayer.capabilities.isFlying = false;
		}
	}
}
