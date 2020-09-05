package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;

public class AutoRespawn extends Mod {

	public AutoRespawn() {
		super("AutoRespawn", "autorespawn");
	}

	@Override
	public void onTick()
	{
		if(enabled && player().isDead)
		{
			player().respawnPlayer();
			mc.displayGuiScreen(null);
		}
	}
}
