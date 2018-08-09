package de.chrissx.mods.movement;

import java.awt.Color;
import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

public class Sneak extends Mod {

	SneakMode mode = SneakMode.PACKET;
	File mf;
	
	public Sneak() {
		super("Sneak");
		mf = getApiFile("mode");
	}

	@Override
	public void onTick() {
		if(enabled)
			if(mode.equals(SneakMode.PACKET))
				;
			else if(mode.equals(SneakMode.BYPASS))
				mc.thePlayer.movementInput.sneak = true;
			else
				Util.sendMessage("Currently your mode is not supported, this should be a bug, please report to chrissx!");
	}
	
	@Override
	public void toggle() {
		enabled = !enabled;
		if(mode.equals(SneakMode.PACKET))
			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, enabled ? Action.START_SNEAKING : Action.STOP_SNEAKING));
	}
	
	@Override
	public boolean onRender(FontRenderer r, int x, int y) {
		if(isEnabled())
			r.drawString(name+"("+mode.toString()+")", x, y, Color.WHITE.getRGB());
		return isEnabled();
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
					Util.sendMessage("�4Error valueOf-ing SneakMode.");
				}
			else
				Util.sendMessage("#sneak to toggle, #sneak mode <SneakMode> to set mode.");
	}

	@Override
	public void apiUpdate() {
		write(mf, mode.b);
	}
}