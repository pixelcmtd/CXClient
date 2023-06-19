package de.chrissx.mods.combat;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.BooleanOption;
import de.chrissx.mods.options.DoubleOption;
import de.chrissx.mods.options.EnumOption;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Random;
import net.minecraft.entity.EntityLivingBase;

public class TriggerBot extends Mod {

	protected DoubleOption range = new DoubleOption("range", "The range in which to attack", 6);
	protected Option<KillauraMode> mode = new EnumOption<KillauraMode>(KillauraMode.class, "mode",
	                                                                   "Whether to attack players, mobs, or both",
	                                                                   new KillauraMode[] { KillauraMode.BOTH, KillauraMode.PLAYERS, KillauraMode.MOBS });
	protected BooleanOption attackInvis = new BooleanOption("invis", "Whether to attack invisible entities", false);
	protected BooleanOption slowdown = new BooleanOption("slowdown", "Whether to slow down randomly", false);
	protected List<String> whitelistedPlayers = new ArrayList<String>();

	public TriggerBot() {
		this("TriggerBot", "Automatically hits when you're facing a player or mob");
	}

	protected TriggerBot(String name, String desc) {
		super(name, desc);
		addOption(range);
		addOption(mode);
		addOption(attackInvis);
		addOption(slowdown);
	}

	// TODO: use mode
	@Override
	public void onTick() {
		if (slowdown.value && Random.rand(4) == 3)
			return;
		if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null
		        && mc.objectMouseOver.entityHit instanceof EntityLivingBase
		        && mc.objectMouseOver.hitVec.distanceTo(player().getEyeVector()) <= range.value
		        && (attackInvis.value || !mc.objectMouseOver.entityHit.isInvisible())
		        && !mc.objectMouseOver.entityHit.isDead
		        && ((EntityLivingBase) mc.objectMouseOver.entityHit).getHealth() > 0) {
			playerController().hitEntity(player(), mc.objectMouseOver.entityHit);
		}

		// TODO: figure this out
		// if (!hc.getMods().noswing.isEnabled())
		// player().swingItem();
	}

	// TODO: remove this last horrible thing (maybe somehow list options?!)
	@Override
	public void processCommand(String[] args) {
		if (args.length > 1 && args[0].equalsIgnoreCase("add"))
			whitelistedPlayers.add(args[1]);
		else if (args.length > 1 && args[0].equalsIgnoreCase("remove"))
			whitelistedPlayers.remove(args[1]);
		else
			super.processCommand(args);
	}
}
