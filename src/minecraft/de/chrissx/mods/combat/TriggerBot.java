package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;

// TODO: options, copied from killaura
public class TriggerBot extends Mod {

	public TriggerBot() {
		super("TriggerBot", "triggerbot", "Automatically hits when you're facing a player or mob");
	}

	@Override
	public void onTick() {
		if (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && mc.objectMouseOver.entityHit != null) {
			player().swingItem();
			playerController().attackEntity(player(), mc.objectMouseOver.entityHit);
		}
	}

}
