package de.chrissx.mods.combat;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.BooleanOption;
import de.chrissx.mods.options.DoubleOption;
import de.chrissx.mods.options.EnumOption;
import de.chrissx.mods.options.Option;

public class TriggerBot extends Mod {

	protected DoubleOption range = new DoubleOption("range", "The range in which to hit", 6);
	protected Option<KillauraMode> mode = new EnumOption<KillauraMode>(KillauraMode.class, "bypass", "Whether to attack players, mobs, or both", new KillauraMode[] {KillauraMode.BOTH, KillauraMode.PLAYERS, KillauraMode.MOBS});
	protected BooleanOption attackInvis = new BooleanOption("invis", "Whether to attack invisible entities", false);
	protected BooleanOption slowdown = new BooleanOption("slowdown", "Whether to slow down randomly", false);
	protected List<String> whitelistedPlayers = new ArrayList<String>();

	public TriggerBot() {
		super("TriggerBot", "Automatically hits when you're facing a player or mob");
		addOption(range);
		addOption(mode);
		addOption(attackInvis);
		addOption(slowdown);
	}

	// TODO: implement `mode`, `attackInvis` and `slowdown`
	@Override
	public void onTick() {
		if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.hitVec.distanceTo(player().getEyeVector()) <= range.value) {
			playerController().hitEntity(player(), mc.objectMouseOver.entityHit);
		}
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
