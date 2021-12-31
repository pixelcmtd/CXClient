package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.FloatOption;

public class ACSpeed1 extends Mod {

	FloatOption speed = new FloatOption("speed", "The speed at which you fall down after jumping", .2f);
	File sf;

	public ACSpeed1() {
		super("Speed-Bypass1", "speedac1", "Makes you faster by jumping really quickly");
		addOption(speed);
		sf = getApiFile("speed");
	}

	@Override
	public void onTick() {
		if (player().onGround)
			player().jump();
		else
			player().motionY -= speed.value;
	}

	@Override
	public String getRenderstring() {
		return name + "(SPEED:" + speed + ")";
	}

	@Override
	public void apiUpdate() {
		write(sf, speed.value);
	}
}
