package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class Autoclicker extends Mod {

	boolean mouseButton = true;
	
	public Autoclicker() {
		super("AutoClicker", "autoclicker");
	}

	@Override
	public void onTick() {
		click(mouseButton);
	}

	@Override
	public String getRenderstring() {
		return name+"("+(mouseButton ? "LEFT" : "RIGHT")+")";
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