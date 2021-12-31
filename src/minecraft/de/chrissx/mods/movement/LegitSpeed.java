package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;

public class LegitSpeed extends Mod {

	public LegitSpeed() {
		super("LegitSpeed", "speedlegit", "Sprints and jumps as is you were a legit player with a hurting hand");
	}

	@Override
	public void onTick() {
		if (player().onGround) {
			player().jump();
			player().setSprinting(true);
		}
	}
}
