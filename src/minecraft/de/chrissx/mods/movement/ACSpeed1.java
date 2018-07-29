package de.chrissx.mods.movement;

import java.awt.Color;

import de.chrissx.Util;
import de.chrissx.mods.Mod;
import net.minecraft.client.gui.FontRenderer;

public class ACSpeed1 extends Mod {

	private float speed = 0.2f;
	
	public ACSpeed1() {
		super("Speed-Bypass1");
	}

	@Override
	public void onTick() {
		if(enabled) {
			if(mc.thePlayer.onGround)
				mc.thePlayer.jump();
			else
				mc.thePlayer.motionY -= speed;
		}
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("speed"))
			try {
				speed = Float.parseFloat(args[2]);
			} catch (Exception e) {
				Util.sendMessage("Error parsing float.");
			}
		else
			Util.sendMessage("#speedac1 to toggle, #speedac1 speed <float> to change speed.");
	}

	@Override
	public boolean onRender(FontRenderer r, int x, int y) {
		if(isEnabled())
			r.drawString(name+"(SPEED:"+speed+")", x, y, Color.WHITE.getRGB());
		return isEnabled();
	}
}
