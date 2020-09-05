package de.chrissx.mods.movement;

import de.chrissx.mods.Semimod;

public class Flip extends Semimod {

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1) toggle();
		else player().rotationYaw += Float.parseFloat(args[1]);
	}

	public Flip() {
		super("Flip", "flip");
	}

	@Override
	public void toggle() {
		player().rotationYaw += 180;
	}
}
