package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.Option;

public class HighJump extends Mod {

	public Option<Double> motion = new Option<Double>("height", "Jump height (default: 2 blocks)", 0.75) {
		@Override
		public void set(String value) {
			double d = Double.parseDouble(value);
			if (d <= 1)
				this.value = 0.42 * d;
			else {
				this.value = 0.42;
				d -= 1;
				if (d <= 1)
					this.value += 0.15 * d;
				else {
					this.value += 0.15;
					d -= 1;
					if (d <= 9)
						this.value += d / 10;
					else {
						this.value += 0.9;
						d -= 9;
						this.value += d * 0.07;
					}
				}
			}
		}
	};

	public HighJump() {
		super("HighJump", "highjump", "Allows you to jump higher");
		addOption(motion);
	}
}
