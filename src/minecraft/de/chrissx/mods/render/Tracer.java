package de.chrissx.mods.render;

import java.awt.Color;

import de.chrissx.locations.LocFloat64;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;

public class Tracer extends Mod {

	public Tracer() {
		super("Tracer", "tracer");
	}

	@Override
	public void onRender(FontRenderer r, int x, int y) {
		r.drawString(name, x, y, Color.WHITE.getRGB());

	    LocFloat64 start = Util.getEyePos();

		for (Entity p : world().loadedEntityList) {
		  	if(p == player()) continue;
		   	Util.drawLine(start, new LocFloat64(p.posX, p.posY, p.posZ), p);
		}
	}
}