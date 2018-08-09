package de.chrissx.mods.movement;

import java.util.Random;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;

public class AntiAfk extends Mod {

	public AntiAfk() {
		super("AntiAFK");
	}
	
	@Override
	public void toggle() {
		enabled = !enabled;
		if(enabled)
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(enabled)
						try {
							int j = Util.rand(50);
							for(int i = 0; i < j; i++) {
								mc.thePlayer.motionX = Util.randDouble()/11;
								mc.thePlayer.motionZ = Util.randDouble()/11;
								Thread.sleep(50);
							}
							for(int i = 0; i < j; i++) {
								mc.thePlayer.motionX = -Util.randDouble()/11;
								mc.thePlayer.motionZ = -Util.randDouble()/11;
								Thread.sleep(50);
							}
							mc.thePlayer.motionX = 0;
							mc.thePlayer.motionZ = 0;
							Thread.sleep(Util.rand(30)*Util.rand(2000));
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}).start();
	}
}