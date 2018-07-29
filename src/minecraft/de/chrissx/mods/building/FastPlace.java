package de.chrissx.mods.building;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;

public class FastPlace extends Mod {

	public FastPlace() {
		super("FastPlace");
	}

	@Override
	public void onTick() {
		if(enabled)
			mc.rightClickDelayTimer = 0;
	}
}