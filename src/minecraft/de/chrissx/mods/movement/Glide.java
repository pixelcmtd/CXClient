package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.DoubleOption;
import net.minecraft.client.entity.EntityPlayerSP;

// TODO: check the difference between this and FastFall
public class Glide extends Mod {

	DoubleOption speed = new DoubleOption("speed", "The falling speed", -0.05);

	public Glide() {
		super("Glide", "glide", "Makes you fall slower, or faster, or up?");
		addOption(speed);
	}

	@Override
	public void onTick() {
		EntityPlayerSP p = player();
		if (p.isAirBorne && !p.isInWater() && !p.isInLava() && !p.isOnLadder() && p.motionY < 0.0)
			p.motionY = speed.value;
	}
}
