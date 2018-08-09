package de.chrissx.mods.render;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import de.chrissx.util.EntityFakePlayer;
import net.minecraft.client.Minecraft;

public class Freecam extends Mod {

	EntityFakePlayer fP;
	
	public Freecam() {
		super("Freecam");
	}

	@Override
	public void onTick() {
		if(enabled) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionZ = 0;
			
			mc.thePlayer.capabilities.isFlying = true;
			if(mc.gameSettings.keyBindJump.isKeyDown())
				mc.thePlayer.motionY += mc.thePlayer.capabilities.getFlySpeed();
			if(mc.gameSettings.keyBindSneak.isKeyDown())
				mc.thePlayer.motionY -= mc.thePlayer.capabilities.getFlySpeed();
		}
	}
	
	@Override
	public void toggle() {
		enabled = !enabled;
		if(enabled) {
			fP = new EntityFakePlayer();
			mc.renderGlobal.loadRenderers();
		}else {
			fP.destruct();
			mc.renderGlobal.loadRenderers();
		}
	}


	@Override
	public void onStop() {
		if(enabled)
			fP.destruct();
	}
}