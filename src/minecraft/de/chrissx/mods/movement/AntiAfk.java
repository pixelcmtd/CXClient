package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.util.Random;

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
							int j = Random.rand(50);
							for(int i = 0; i < j; i++) {
								mc.thePlayer.motionX = Random.randDouble()/11;
								mc.thePlayer.motionZ = Random.randDouble()/11;
								Thread.sleep(50);
							}
							for(int i = 0; i < j; i++) {
								mc.thePlayer.motionX = -Random.randDouble()/11;
								mc.thePlayer.motionZ = -Random.randDouble()/11;
								Thread.sleep(50);
							}
							mc.thePlayer.motionX = 0;
							mc.thePlayer.motionZ = 0;
							Thread.sleep(Random.rand(30)*Random.rand(2000));
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}).start();
	}
}