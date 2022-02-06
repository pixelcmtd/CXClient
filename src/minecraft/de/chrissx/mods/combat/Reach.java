package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.FloatOption;

public class Reach extends Mod {

	// TODO: hä?! (according to `PlayerControllerMP`, it isn't 6 in vanilla)
	FloatOption range = new FloatOption("range", "The range (6 in Vanilla)", 7);

	public Reach() {
		super("Reach", "Increases your range");
		addOption(range);
	}

	public float getReach() {
		return range.value;
	}
}
