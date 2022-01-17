package de.chrissx.mods;

import de.chrissx.util.Random;

public class Afk extends Mod {

	public Afk() {
		super("AFK", "afk", "Messes with AFK sensing and reduces load");
	}

	// TODO: a better movement pattern
	// TODO: less rendering
	// NOTE: a reconnect would be cool, but that difficult to implement and unnecessary
	@Override
	public void toggle() {
		enabled = !enabled;
		if (enabled)
			new Thread(new Runnable() {
			@Override
			public void run() {
				while (enabled)
					try {
						int j = Random.rand(50);
						for (int i = 0; i < j; i++) {
							player().motionX = Random.randDouble() / 11;
							player().motionZ = Random.randDouble() / 11;
							Thread.sleep(50);
						}
						for (int i = 0; i < j; i++) {
							player().motionX = -Random.randDouble() / 11;
							player().motionZ = -Random.randDouble() / 11;
							Thread.sleep(50);
						}
						player().motionX = 0;
						player().motionZ = 0;
						Thread.sleep(Random.rand(30) * Random.rand(2000));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}).start();
	}
}