package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Parkour extends Mod {

	public Parkour() {
		super("Parkour");
	}

	@Override
	public void onTick()
	{
		if (enabled && mc.thePlayer.onGround && !mc.thePlayer.isSneaking() && !mc.gameSettings.keyBindSneak.isPressed() && !mc.gameSettings.keyBindJump.isPressed()
				&& mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty())
			mc.thePlayer.jump();
	}
}
