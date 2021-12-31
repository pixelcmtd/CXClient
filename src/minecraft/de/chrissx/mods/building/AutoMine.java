package de.chrissx.mods.building;

import de.chrissx.mods.Mod;

public class AutoMine extends Mod {

	public AutoMine() {
		super("AutoMine", "automine", "Automatically breaks every block that you look at");
	}

	@Override
	public void onTick() {
		if (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && !settings().keyBindAttack.pressed)
			settings().keyBindAttack.pressed = !world().isAirBlock(mc.objectMouseOver.getBlockPos());
	}
}
