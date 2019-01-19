package de.chrissx.mods.fun;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;

public class Throw implements Bindable, Commandable {

	long throwCount = 500;
	long delay = 1;
	Minecraft mc = Minecraft.getMinecraft();

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
				Util.sendMessage("\u00a74Error parsing longs.");
			}
			toggle();
		}
	}
	
	void toggle()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(long i = 0; i < throwCount; i++) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mc.rightClickMouse();
					Util.sendMessage((i + 1) + "/" + throwCount + " thrown.");
				}
			}
		}).start();
	}

	@Override
	public String getName() {
		return "Throw";
	}

	@Override
	public void onHotkey() {
		toggle();
	}
}