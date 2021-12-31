package de.chrissx.mods.render;

import java.awt.Color;

import de.chrissx.locations.Loc;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;

public class Tracer extends Mod {

	public Tracer() {
		super("Tracer", "tracer", "Draws lines from the middle of the screen to every entity around you");
	}

	// FIXME: doesnt work, wtf
	@Override
	public void onRender(FontRenderer r, int x, int y) {
		r.drawString(name, x, y, Color.WHITE.getRGB());

		Loc<Double> start = Util.getEyePos();

		for (Entity p : world().loadedEntityList) {
			if (p == player())
				continue;
			Util.drawLine(start, new Loc<Double>(p.posX, p.posY, p.posZ), p);
		}
	}
}