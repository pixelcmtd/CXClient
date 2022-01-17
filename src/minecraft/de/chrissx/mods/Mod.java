package de.chrissx.mods;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IChatComponent;

public abstract class Mod extends Semimod implements TickListener, StopListener, RenderedObject, ChatBot {

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
		return name;
	}

	@Override
	public void onRender(FontRenderer r, int x, int y) {
		r.drawString(getRenderstring(), x, y, Color.WHITE.getRGB());
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onTick() {
	}

	@Override
	public void onChatMessage(IChatComponent component) {
	}
}