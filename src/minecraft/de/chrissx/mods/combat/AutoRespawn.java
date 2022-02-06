package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;

public class AutoRespawn extends Mod {

	public AutoRespawn() {
		super("AutoRespawn", "Respawns when you die");
	}

	@Override
	public void onTick() {
		if (player().isDead) {
			player().respawnPlayer();
			mc.displayGuiScreen(null);
		}
	}
}
