package de.chrissx.mods.chat;

import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.util.Random;
import de.chrissx.util.Util;
import net.minecraft.entity.player.EntityPlayer;

public class MassTpa extends Mod {

	List<EntityPlayer> players;

	public MassTpa() {
		super("MassTPA", "masstpa", "Send a /tpa request to all players on the server");
	}

	@Override
	public void onTick() {
		// dont spam too much
		if (Random.rand(20) != 5)
			return;
		int i = Random.rand(players.size() - 1);
		Util.sendChat("/tpa " + players.remove(i).getName());
		if (players.size() == 0)
			toggle();
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		players = world().playerEntities;
		if (players.size() < 2)
			enabled = false;
	}
}