package de.chrissx.mods.render;

import de.chrissx.mods.Mod;
import de.chrissx.util.EntityFakePlayer;
import de.chrissx.util.Util;

public class Freecam extends Mod {

	EntityFakePlayer fP;

	public Freecam() {
		super("Freecam");
	}

	@Override
	public void onTick() {
		if(enabled) {
			mc.thePlayer.capabilities.isFlying = true;
			mc.thePlayer.noClip = true;
			mc.thePlayer.onGround = false;
			mc.thePlayer.fallDistance = 0;
			
			if(mc.gameSettings.keyBindJump.isKeyDown())
				mc.thePlayer.motionY = mc.thePlayer.capabilities.getFlySpeed();
			if(mc.gameSettings.keyBindSneak.isKeyDown())
				mc.thePlayer.motionY = -mc.thePlayer.capabilities.getFlySpeed();
		}
	}

	@Override
	public void toggle() {
		mc.thePlayer.motionY = 0;
		enabled = !enabled;
		if(enabled) {
			Util.sendMessage("\u00a74You should not use this unless you're in SP, because it's not done yet.");
			fP = new EntityFakePlayer();
		}else {
			fP.destruct();
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.noClip = false;
		}
		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onStop() {
		if(enabled)
			fP.destruct();
	}
}