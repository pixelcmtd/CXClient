package de.chrissx.mods;

public class Panic extends Semimod {

	public Panic() {
		super("Panic");
	}

	@Override
	public void toggle() {
		for(Mod m : hc.getMods())
			if(m.enabled)
				m.toggle();
	}

}
