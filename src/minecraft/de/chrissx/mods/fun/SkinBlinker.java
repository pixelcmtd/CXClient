package de.chrissx.mods.fun;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinBlinker extends Mod {

	public SkinBlinker() {
		super("SkinBlinker");
	}
	
	byte b = 0;

	@Override
	public void onTick() {
		if(enabled) {
			if(b == 0)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.HAT);
			else if(b == 1)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.JACKET);
			else if(b == 2)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.CAPE);
			else if(b == 3)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG);
			else if(b == 4)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG);
			else if(b == 5)
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE);
			else
				mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE);
			if(b < 6) {
				b++;
			}else {
				b = 0;
			}
		}
	}
}