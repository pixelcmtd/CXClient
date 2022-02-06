package de.chrissx.mods.building;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.FloatOption;

public class FastBreak extends Mod {

	// TODO: detailed desc
	public FloatOption speed = new FloatOption("speed", "The divisor", 2);

	public FastBreak() {
		super("FastBreak", "Makes you break blocks faster");
		addOption(speed);
	}

	@Override
	public String getRenderstring() {
		return name + "(SPEED:" + speed + ")";
	}
}
