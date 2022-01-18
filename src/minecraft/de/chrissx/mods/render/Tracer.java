package de.chrissx.mods.render;

import java.awt.Color;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class Tracer extends Mod {

	public Tracer() {
		super("Tracer", "tracer", "Draws lines from the middle of the screen to every entity around you");
	}

	// FIXME: doesnt work, wtf
	@Override
	public void onRender(FontRenderer r, int x, int y) {
		r.drawString(name, x, y, Color.WHITE.getRGB());

		Vec3 start = player().getEyeVector();

		for (Entity p : world().loadedEntityList) {
			if (p == player())
				continue;
			Util.drawLine(start, new Vec3(p.posX, p.posY, p.posZ), p);
		}
	}
}