package de.chrissx.mods;

import net.minecraft.client.gui.FontRenderer;

public interface RenderedObject {

	public void onRender(FontRenderer r, int x, int y);
	public String getRenderstring();
	public boolean isEnabled();

}
