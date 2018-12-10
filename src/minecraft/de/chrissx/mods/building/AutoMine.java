package de.chrissx.mods.building;

import de.chrissx.mods.Mod;

public class AutoMine extends Mod {

	public AutoMine() {
		super("AutoMine");
	}

	@Override
	public void onTick()
	{
		if(enabled && mc.objectMouseOver != null &&
				mc.objectMouseOver.getBlockPos() != null &&
				!mc.gameSettings.keyBindAttack.pressed)
			mc.gameSettings.keyBindAttack.pressed =
			!mc.theWorld.isAirBlock(mc.objectMouseOver.getBlockPos());
	}
}
