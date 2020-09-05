package de.chrissx.mods;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IChatComponent;

public abstract class Mod extends Semimod implements TickListener, StopListener, RenderedObject, ChatBot {

	protected volatile boolean enabled = false;

	protected Mod(String name, String argv0)
	{
		super(name, argv0);
	}

	public void toggle()
	{
		enabled = !enabled;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	@Override
	public String getRenderstring()
	{
		return name;
	}

	@Override
	public boolean onRender(FontRenderer r, int x, int y)
	{
		if(enabled)
			r.drawString(getRenderstring(), x, y, Color.WHITE.getRGB());
		return enabled;
	}

	@Override
	public void onStop() {}
	@Override
	public void onTick() {}
	@Override
	public void onChatMessage(IChatComponent component) {}
	public void apiUpdate() {}
}