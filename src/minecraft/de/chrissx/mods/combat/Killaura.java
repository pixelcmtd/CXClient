package de.chrissx.mods.combat;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.BooleanOption;
import de.chrissx.mods.options.DoubleOption;
import de.chrissx.mods.options.EnumOption;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Random;
import de.chrissx.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

// TODO: inherit from triggerbot
// TODO: lets try to detect anticheats by not attacking players that havent
//       moved yet and spawned in the last second or so, maybe as an option
public class Killaura extends Mod {

	// TODO: rename some (of the legit) options
	DoubleOption max_range = new DoubleOption("range", "The range in which to kill", 6);
	Option<KillauraMode> mode = new EnumOption<KillauraMode>(KillauraMode.class, "bypass", "Whether to attack players, mobs, or both", new KillauraMode[] {KillauraMode.BOTH, KillauraMode.PLAYERS, KillauraMode.MOBS});
	BooleanOption attackInvis = new BooleanOption("invis", "Whether to attack invisible entities", false);
	BooleanOption legit1 = new BooleanOption("legit1", "Whether to target the attacked entities", false);
	BooleanOption legit2 = new BooleanOption("legit1", "Whether to sometimes miss randomly", false);
	BooleanOption slowdown = new BooleanOption("slowdown", "Whether to slow down randomly", false);
	List<String> whitelistedPlayers = new ArrayList<String>();

	public Killaura() {
		super("KillAura", "Attacks all the players and mobs around you");
		addOption(max_range);
		addOption(attackInvis);
		addOption(legit1);
		addOption(legit2);
		addOption(slowdown);
		addOption(mode);
	}

	@Override
	public void onTick() {
		if ((legit1.value || legit2.value) && player().isEating())
			return;
		if (slowdown.value && Random.rand(4) == 3)
			return;
		for (Entity e : world().loadedEntityList) {
			if (!(e instanceof EntityLivingBase) || e == player() || player().getDistanceToEntity(e) > max_range.value
			        || (!attackInvis.value && e.isInvisible()) || e.isDead
			        || ((EntityLivingBase) e).getHealth() <= 0) /* skip while entity is dying */
				continue;
			else {
				boolean attack = Random.rand(3) == 2;
				boolean miss = (Random.randBool() && Random.randBool());

				if (legit1.value && attack && !miss)
					Util.faceBounds(e.boundingBox);
				if (!hc.getMods().noswing.isEnabled() && (attack || !legit2.value))
					player().swingItem();
				if (!legit2.value || (attack && !miss))
					playerController().attackEntity(player(), e);
				if (legit1.value)
					return;
			}
		}
	}

	@Override
	public String getRenderstring() {
		return name + "(RANGE:" + max_range.value + ",MODE:" + mode.value + ",INVIS:" + (attackInvis.value ? "YA" : "NA")
		       + ",LEGIT1:" + (legit1.value ? "YA" : "NA") + ",LEGIT2:" + (legit2.value ? "YA" : "NA") + ",SD:" + slowdown.value + ")";
	}

	// TODO: remove this last horrible thing (maybe somehow list options?!)
	@Override
	public void processCommand(String[] args) {
		if(args.length > 1 && args[0].equalsIgnoreCase("add"))
			whitelistedPlayers.add(args[1]);
		else if(args.length > 1 && args[0].equalsIgnoreCase("remove"))
			whitelistedPlayers.remove(args[1]);
		else
			super.processCommand(args);
	}
}