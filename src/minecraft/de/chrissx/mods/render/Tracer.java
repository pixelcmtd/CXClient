package de.chrissx.mods.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.chrissx.locations.LocFloat64;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

public class Tracer extends Mod {

	public Tracer() {
		super("Tracer");
	}

	@Override
	public boolean onRender(FontRenderer r, int x, int y) {
		if(enabled) {
			r.drawString(name, x, y, Color.WHITE.getRGB());

			GL11.glPointSize(2.0f);

		    LocFloat64 start = Util.getEyePos();

		    for (EntityPlayer entity : world().playerEntities) {
		        	LocFloat64 end = new LocFloat64(x, y, y);
		        	drawLine(start.x, start.y, start.z, end.x, end.y, end.z, player().getDistanceToEntity(entity));
		    }
		}
		return enabled;
	}

	void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, float blocks) {
		GL11.glBegin(GL11.GL_POINTS);
		
	    int samples = (int)(15*blocks);
	    
	    double dx = (x2-x1)/samples;
	    double dy = (y2-y1)/samples;
	    double dz = (z2-z2)/samples;
	    
	    for(int i = 0; i < samples; i++)
	    	GL11.glVertex3d(i*dx, i*dy, i*dz);
	    
	    GL11.glEnd();
	}
}