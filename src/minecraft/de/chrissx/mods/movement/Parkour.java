package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class Parkour extends Mod {

	public Parkour() {
		super("Parkour");
	}

	@Override
	public void onTick()
	{
		if (enabled && player().onGround && !player().isSneaking() && !settings().keyBindSneak.isPressed() && !settings().keyBindJump.isPressed()
				&& world().getCollidingBoundingBoxes(player(), player().getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty())
			player().jump();
	}
}
