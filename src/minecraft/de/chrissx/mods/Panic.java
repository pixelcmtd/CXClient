package de.chrissx.mods;

public class Panic extends Semimod {

	public Panic() {
		super("Panic", "panic", "Quickly disables all mods");
	}

	@Override
	public void toggle() {
		for (Mod m : hc.getMods().mods)
			if (m.enabled)
				m.toggle();
	}

}
