package de.chrissx.mods.combat;

import de.chrissx.mods.options.BooleanOption;
import de.chrissx.util.Random;
import de.chrissx.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

// TODO: lets try to detect anticheats by not attacking players that havent
//       moved yet and spawned in the last second or so, maybe as an option
public class Killaura extends TriggerBot {

	// TODO: rename legit1 and legit2
	// TODO: WTF MODE ISNT USED
	BooleanOption legit1 = new BooleanOption("legit1", "Whether to target the attacked entities", false);
	BooleanOption legit2 = new BooleanOption("legit2", "Whether to sometimes miss randomly", false);

	public Killaura() {
		super("KillAura", "Attacks all the players and mobs around you");
		addOption(legit1);
		addOption(legit2);
	}

	@Override
	public void onTick() {
		if ((legit1.value || legit2.value) && player().isEating())
			return;
		if (slowdown.value && Random.rand(4) == 3)
			return;
		for (Entity e : world().loadedEntityList) {
			if (!(e instanceof EntityLivingBase) || e == player() || player().getDistanceToEntity(e) > range.value
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
		return name + "(RANGE:" + range.value + ",MODE:" + mode.value + ",INVIS:"
				+ (attackInvis.value ? "YA" : "NA") + ",LEGIT1:" + (legit1.value ? "YA" : "NA") + ",LEGIT2:"
				+ (legit2.value ? "YA" : "NA") + ",SD:" + slowdown.value + ")";
	}
}
