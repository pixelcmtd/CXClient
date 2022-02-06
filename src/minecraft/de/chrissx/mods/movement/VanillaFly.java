package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.Option;

public class VanillaFly extends Mod {

	public VanillaFly() {
		super("VanillaFly", "Makes you fly like in creative mode");
		setArgv0("flyvanilla");
		addOption(new Option<Float>("speed", "The speed at which you fly", 0.05f) {
			@Override
			public void set(String value) {
				setSpeed(this.value = Float.parseFloat(value));
			}
		});
	}

	public void setSpeed(float speed) {
		player().capabilities.setFlySpeed(speed);
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		try {
			// TODO: fix this for creative mode
			player().capabilities.allowFlying = enabled;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
