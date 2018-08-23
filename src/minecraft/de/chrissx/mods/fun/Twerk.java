package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;
import net.minecraft.client.settings.KeyBinding;

public class Twerk extends Mod {

	public Twerk() {
		super("Twerk");
	}

	@Override
	public void onTick() {
		if(enabled)
		{
			KeyBinding sneak = mc.gameSettings.keyBindSneak;
			sneak.pressed = !sneak.pressed;
		}
	}
}
