package de.chrissx.mods.movement;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Flip implements Bindable, Commandable {

	EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
	
	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			p.rotationYaw += 180;
		else
			p.rotationYaw += Float.parseFloat(args[1]);
	}

	@Override
	public void onHotkey() {
		p.rotationYaw += 180;
	}

	@Override
	public String getName() {
		return "Flip";
	}

}
