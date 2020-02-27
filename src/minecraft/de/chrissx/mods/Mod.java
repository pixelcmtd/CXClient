package de.chrissx.mods;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import com.google.common.io.Files;

import de.chrissx.util.Consts;
import net.minecraft.client.gui.FontRenderer;

public abstract class Mod extends Semimod implements TickListener, StopListener, RenderedObject {

	protected volatile boolean enabled = false;

	protected Mod(String name)
	{
		super(name);
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
	public void apiUpdate() {}
}