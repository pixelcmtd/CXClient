package de.chrissx.mods.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.chrissx.LocDouble;
import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vector3d;

public class Tracer extends Mod {

	public Tracer() {
		super("Tracer");
	}
	
	@Override
	public boolean onRender(FontRenderer r, int x, int y) {
		if(enabled) {
			r.drawString(name, x, y, Color.WHITE.getRGB());
		    
			GL11.glPointSize(2.0f);
			
		    LocDouble start = Util.getEyesPos();
		    
		    for (EntityPlayer entity : mc.theWorld.playerEntities) {
		        	LocDouble end = new LocDouble(x, y, y);
		        	drawLine(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ(), mc.thePlayer.getDistanceToEntity(entity));
		    }
		}
		return enabled;
	}
	
	private void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, float blocks) {
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