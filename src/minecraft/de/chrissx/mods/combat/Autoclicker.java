package de.chrissx.mods.combat;

import java.awt.Color;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;

public class Autoclicker extends Mod {

	boolean mouseButton = true;
	
	public Autoclicker() {
		super("AutoClicker");
	}

	@Override
	public void onTick() {
		if(enabled)
			if(mouseButton)
				mc.clickMouse();
			else
				mc.rightClickMouse();
	}
	
	@Override
	public boolean onRender(FontRenderer r, int x, int y) {
		if(enabled)
			r.drawString(name+"("+(mouseButton ? "LEFT" : "RIGHT")+")", x, y, Color.WHITE.getRGB());
		return enabled;
	}

	@Override
	public void onStop() {}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 2 && args[1].equalsIgnoreCase("button"))
			mouseButton = !mouseButton;
		else
			Util.sendMessage("#autoclicker to toggle, #autoclicker button to change the button.");
	}
}