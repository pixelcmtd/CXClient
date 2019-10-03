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
	protected final String apiDir;

	protected Mod(String name)
	{
		super(name);
		apiDir = Paths.get(Consts.modsPath, name).toString();
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

	public String getApiDir()
	{
		return apiDir;
	}

	protected File getApiFile(String name)
	{
		return Paths.get(apiDir, name).toFile();
	}

	protected void write(File f, byte b)
	{
		try {
			Files.write(new byte[] {b}, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, byte[] b)
	{
		try {
			Files.write(b, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, boolean b)
	{
		try {
			Files.write(new byte[] {(byte) (b ? 1 : 0)}, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, String s, Charset cs)
	{
		try {
			Files.write(cs.encode(s).array(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, int i)
	{
		try {
			Files.write(new byte[] {(byte)(i >> 24), (byte)(i >> 16), (byte)(i >> 8), (byte)i}, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, long i)
	{
		try {
			Files.write(new byte[] {(byte)(i >> 56), (byte)(i >> 48), (byte)(i >> 40), (byte)(i >> 32),
					(byte)(i >> 24), (byte)(i >> 16), (byte)(i >> 8), (byte)i}, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, short i)
	{
		try {
			Files.write(new byte[] {(byte)(i >> 8), (byte)i}, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, float g)
	{
		try {
			Files.write(ByteBuffer.allocate(4).putFloat(g).array(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void write(File f, double d)
	{
		try {
			Files.write(ByteBuffer.allocate(8).putDouble(d).array(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}