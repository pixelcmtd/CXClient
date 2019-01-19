package de.chrissx.mods.fun;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;

public class MultiText implements Bindable, Commandable {
	
	Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void processCommand(String[] args) {
		if(args.length < 2) {
			Util.sendMessage("\u00a74Not enough args.");
			return;
		}else {
			if(args.length > 37) {
				Util.sendMessage("\u00a74You can't have more than 36 items in your inventory.");
				return;
			}
			for(int i = 1; i < args.length; i++) {
				Util.cheatArmorStand(args[i],
						mc.thePlayer.posX,
						mc.thePlayer.posY+((args.length-2)*0.3)-(i*0.3),
						mc.thePlayer.posZ,
						Consts.packetPlayerInventorySlots[i-1]);
			}
		}
	}

	@Override
	public void onHotkey() {
		processCommand(new String[] {"#multitext",
				"CXClient is the best client in the world!",
				"Writing random text is easily possible because your server kinda sucks.",
				"Just fix that by installing Creative Item Control!"});
	}

	@Override
	public String getName() {
		return "MultiText";
	}
}
