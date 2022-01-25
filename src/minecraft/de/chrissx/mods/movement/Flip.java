package de.chrissx.mods.movement;

import de.chrissx.mods.Semimod;

public class Flip extends Semimod {

	public Flip() {
		super("Flip", "flip", "Turns by 180 degrees or an amount you specify");
	}

	@Override
	public void processCommand(String[] args) {
		player().rotationYaw += args.length < 1 ? 180 : Float.parseFloat(args[0]);
	}

	@Override
	public void toggle() {
		player().rotationYaw += 180;
	}
}
