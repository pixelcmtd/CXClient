//THIS IS CLEARLY NOT DONE YET

package de.chrissx.mods.building;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import de.chrissx.mods.Commandable;
import de.chrissx.mods.RenderedObject;
import de.chrissx.mods.StopListener;
import de.chrissx.mods.TickListener;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;

public class MasterBuildersBot implements Commandable, TickListener, StopListener, RenderedObject {

	boolean enabled = false;
	BufferedImage drawAfter;
	
	@Override
	public boolean onRender(FontRenderer r, int x, int y) {
		if(enabled)
			r.drawString("MasterBuildersBot", x, y, Color.WHITE.getRGB());
		return enabled;
	}
	
	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			Util.sendMessage("#masterbuildersbot <name of the theme>");
		else {
			try {
				drawAfter = Util.scale(new BufferedImage(0, 0, 0), 33, 33); //FIRST GOOGLE IMAGE
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return "MasterBuildersBot";
	}

	@Override
	public void onTick() {}

	@Override
	public void onStop() {}
}
