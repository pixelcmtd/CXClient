package de.chrissx.mods.building;

import de.chrissx.mods.Mod;

public class AutoMine extends Mod {

	public AutoMine() {
		super("AutoMine", "automine");
	}

	@Override
	public void onTick()
	{
		if(enabled && mc.objectMouseOver != null &&
				mc.objectMouseOver.getBlockPos() != null &&
				!settings().keyBindAttack.pressed)
			settings().keyBindAttack.pressed =
			!world().isAirBlock(mc.objectMouseOver.getBlockPos());
	}
}
