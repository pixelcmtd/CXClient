package de.chrissx.mods.render;

import de.chrissx.mods.Mod;
import de.chrissx.mods.StopListener;
import de.chrissx.util.EntityFakePlayer;
import de.chrissx.util.Util;

// TODO: make this thing work at all
public class Freecam extends Mod implements StopListener {

	EntityFakePlayer fP;

	public Freecam() {
		super("Freecam", "experimental-freecam", "An experimental freecam mod that may not work");
	}

	@Override
	public void onTick() {
		player().capabilities.isFlying = true;
		player().noClip = true;
		player().onGround = false;
		player().fallDistance = 0;

		if (settings().keyBindJump.isKeyDown())
			player().motionY = player().capabilities.getFlySpeed();
		if (settings().keyBindSneak.isKeyDown())
			player().motionY = -player().capabilities.getFlySpeed();
	}

	@Override
	public void toggle() {
		player().motionY = 0;
		enabled = !enabled;
		if (enabled) {
			Util.sendMessage("\u00a74You should not use this unless you're in SP, because it's not done yet.");
			fP = new EntityFakePlayer();
		} else {
			fP.destruct();
			player().capabilities.isFlying = false;
			player().noClip = false;
		}
		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onStop() {
		if (enabled)
			fP.destruct();
	}
}