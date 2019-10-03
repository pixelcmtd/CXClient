package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinBlinker extends Mod {

	public SkinBlinker() {
		super("SkinBlinker");
	}
	
	int i = 0;

	@Override
	public void onTick() {
		if(enabled) {
			if(i == 0)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.HAT);
			else if(i == 1)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.JACKET);
			else if(i == 2)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.CAPE);
			else if(i == 3)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG);
			else if(i == 4)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG);
			else if(i == 5)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE);
			else
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE);

			if(i < 6)
				i++;
			else
				i = 0;
		}
	}
}