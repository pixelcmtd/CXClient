package de.chrissx.mods.fun;

import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;

public class Throw extends Mod {

	public Throw() {
		super("Throw");
	}

	long throwCount = 500;
	long delay = 1;

	@Override
	public void processCommand(String[] args) {
		if(args.length < 3 || args.length > 3) {
			Util.sendMessage("#throw <long/count> <long/delay>");
			return;
		}else {
			try {
				throwCount = Long.parseLong(args[1]);
				delay = Long.parseLong(args[2]);
			}catch (Exception e) {
				Util.sendMessage("§4Error parsing longs.");
			}
			toggle();
		}
	}
	
	@Override
	public void toggle() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				enabled = true;
				for(long i = 0; i < throwCount; i++) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mc.rightClickMouse();
				}
				enabled = false;
			}
		}).start();
	}
}