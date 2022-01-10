package de.chrissx.mods.combat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.util.Random;
import de.chrissx.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

// TODO: inherit from triggerbot
public class Killaura extends Mod {

	// TODO: rename some (of the legit) options
	double max_range = 6;
	KillauraMode mode = KillauraMode.BOTH;
	boolean attackInvis = false, legit1 = false, legit2 = false, slowdown = false;
	List<String> whitelistedPlayers = new ArrayList<String>();
	File rf, mf, aif, lf, lf2, sd;

	public Killaura() {
		super("KillAura", "killaura", "Attacks all the players and mobs around you");
		rf = getApiFile("range");
		mf = getApiFile("mode");
		aif = getApiFile("attack_invis");
		lf = getApiFile("legit");
		lf2 = getApiFile("legit2");
		sd = getApiFile("slowdown");
	}

	@Override
	public void onTick() {
		if ((legit1 || legit2) && player().isEating())
			return;
		if (slowdown && Random.rand(4) == 3)
			return;
		for (Entity e : world().loadedEntityList) {
			if (!(e instanceof EntityLivingBase) || e == player() || player().getDistanceToEntity(e) > max_range
			        || (!attackInvis && e.isInvisible()) || e.isDead
			        || ((EntityLivingBase) e).getHealth() <= 0) /* skip while entity is dying */
				continue;
			else {
				boolean attack = Random.rand(3) == 2;
				boolean miss = (Random.randBool() && Random.randBool());

				if (legit1 && attack && !miss)
					Util.faceBounds(e.boundingBox);
				if (!hc.getMods().noswing.isEnabled() && (attack || !legit2))
					player().swingItem();
				if (!legit2 || (attack && !miss))
					playerController().attackEntity(player(), e);
				if (legit1)
					return;
			}
		}
	}

	@Override
	public String getRenderstring() {
		return name + "(RANGE:" + max_range + ",MODE:" + mode.toString() + ",INVIS:" + (attackInvis ? "YA" : "NA")
		       + ",LEGIT1:" + (legit1 ? "YA" : "NA") + ",LEGIT2:" + (legit2 ? "YA" : "NA") + ",SD:" + slowdown + ")";
	}

	// TODO: this is like semi-trivial to get rid of, too
	@Override
	public void processCommand(String[] args) {
		if (args.length == 1)
			toggle();
		else {
			if (args[1].equalsIgnoreCase("range"))
				try {
					max_range = Double.parseDouble(args[2]);
				} catch (Exception e) {
					Util.sendError("Error parsing double.");
				} else if (args[1].equalsIgnoreCase("mode"))
				try {
					mode = KillauraMode.valueOf(args[2].toUpperCase());
				} catch (Exception e) {
					Util.sendError("Error valueOf-ing KillauraMode.");
				} else if (args[1].equalsIgnoreCase("invis"))
				attackInvis = !attackInvis;
			else if (args[1].equalsIgnoreCase("add"))
				whitelistedPlayers.add(args[2]);
			else if (args[1].equalsIgnoreCase("remove"))
				whitelistedPlayers.remove(args[2]);
			else if (args[1].equalsIgnoreCase("legit1"))
				legit1 = !legit1;
			else if (args[1].equalsIgnoreCase("legit2"))
				legit2 = !legit2;
			else if (args[1].equalsIgnoreCase("slowdown"))
				slowdown = !slowdown;
			else
				Util.sendMessage(
				    "#killaura to toggle, #killaura range <double> to set range, #killaura mode <KillauraMode> to set mode, "
				    + "#killaura invis to toggle invis-attacking, #killaura add <String> to add whitelisted player, #killaura remove to remove whitelisted player, "
				    + "#killaura legit1 to toggle targetting, #killaura legit2 to toggle missing, #killaura slowdown to make it slower.");
		}
	}

	@Override
	public void apiUpdate() {
		write(rf, max_range);
		write(mf, mode.b);
		write(aif, attackInvis);
		write(lf, legit1);
		write(lf2, legit2);
		write(sd, slowdown);
	}
}