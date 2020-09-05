package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.util.Random;

public class AntiAfk extends Mod {

	public AntiAfk() {
		super("AntiAFK", "antiafk");
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
								player().motionX = Random.randDouble()/11;
								player().motionZ = Random.randDouble()/11;
								Thread.sleep(50);
							}
							for(int i = 0; i < j; i++) {
								player().motionX = -Random.randDouble()/11;
								player().motionZ = -Random.randDouble()/11;
								Thread.sleep(50);
							}
							player().motionX = 0;
							player().motionZ = 0;
							Thread.sleep(Random.rand(30)*Random.rand(2000));
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}).start();
	}
}