package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinBlinker extends Mod {

	public SkinBlinker() {
		super("SkinBlinker", "skinblink",
		      "Makes your skin \"blink\" by taking off and on the outer layers of your skin");
	}

	int i = 0;

	@Override
	public void onTick() {
		if (i == 0)
			settings().switchModelPartEnabled(EnumPlayerModelParts.HAT);
		else if (i == 1)
			settings().switchModelPartEnabled(EnumPlayerModelParts.JACKET);
		else if (i == 2)
			settings().switchModelPartEnabled(EnumPlayerModelParts.CAPE);
		else if (i == 3)
			settings().switchModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG);
		else if (i == 4)
			settings().switchModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG);
		else if (i == 5)
			settings().switchModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE);
		else
			settings().switchModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE);

		if (i < 6)
			i++;
		else
			i = 0;
	}
}