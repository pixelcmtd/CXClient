package de.chrissx.mods.render;

import de.chrissx.mods.Mod;
import net.minecraft.client.settings.GameSettings;

public class Fullbright extends Mod {

	float before;
	
	public Fullbright() {
		super("FullBright", "fullbright");
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		GameSettings gs = settings();
		if(enabled) {
			before = gs.gammaSetting;
			gs.gammaSetting = 100;
		}else
			gs.gammaSetting = before;
		mc.renderGlobal.loadRenderers();
	}
}