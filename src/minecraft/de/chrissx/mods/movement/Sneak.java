package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

public class Sneak extends Mod {

	SneakMode mode = SneakMode.PACKET;
	File mf;

	public Sneak() {
		super("Sneak", "sneak");
		mf = getApiFile("mode");
	}

	@Override
	public void onTick() {
		if(mode.equals(SneakMode.BYPASS))
			settings().keyBindSneak.pressed = true;
		else if(!mode.equals(SneakMode.PACKET))
			Util.sendMessage("Currently your mode is not supported, this should be a bug, please report to chrissx!");
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		if(mode.equals(SneakMode.PACKET))
			sendPacket(new C0BPacketEntityAction(player(), enabled ? Action.START_SNEAKING : Action.STOP_SNEAKING));
	}

	@Override
	public String getRenderstring() {
		return name + "(" + mode + ")";
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else
			if(args[1].equalsIgnoreCase("mode"))
				try {
					mode = SneakMode.valueOf(args[2].toUpperCase());
				} catch (Exception e) {
					Util.sendMessage("\u00a74Error valueOf-ing SneakMode.");
				}
			else
				Util.sendMessage("#sneak to toggle, #sneak mode <SneakMode> to set mode.");
	}

	@Override
	public void apiUpdate() {
		write(mf, mode.b);
	}
}