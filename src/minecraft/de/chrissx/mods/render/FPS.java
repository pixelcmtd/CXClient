package de.chrissx.mods.render;

import java.util.HashMap;
import java.util.Map;

import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;

public class FPS extends Mod {

	public FPS() {
		super("#JFPS", "Displays the number of frames per second");
		setArgv0("fps");
	}

	@Override
	public String getRenderstring() {
		return Minecraft.getDebugFPS() + " FPS";
	}

	@Override
	public Map<String, Object> apiValues() {
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put("fps", Minecraft.getDebugFPS());
		return vals;
	}

}
