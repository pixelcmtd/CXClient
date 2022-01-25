package de.chrissx.mods;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;

public abstract class Mod extends Semimod implements RenderedObject, TickListener {

	// FIXME: why this?!
	protected volatile boolean enabled = false;

	protected Mod(String name, String argv0, String description) {
		super(name, argv0, description);
	}

	public void toggle() {
		enabled = !enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getRenderstring() {
		// TODO: include `options` by default
		return name;
	}

	@Override
	public void onRender(FontRenderer r, int x, int y) {
		r.drawString(getRenderstring(), x, y, Color.WHITE.getRGB());
	}

	@Override
	public void onTick() {
	}
}