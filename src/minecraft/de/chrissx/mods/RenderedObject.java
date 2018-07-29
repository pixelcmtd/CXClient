package de.chrissx.mods;

import net.minecraft.client.gui.FontRenderer;

public interface RenderedObject {

	public boolean onRender(FontRenderer r, int x, int y);
	
	public String getName();
	
}
