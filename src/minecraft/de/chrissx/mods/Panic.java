package de.chrissx.mods;

import de.chrissx.HackedClient;

public class Panic extends Semimod {

	public Panic() {
		super("Panic");
	}

	@Override
	public void toggle() {
		for(Mod m : HackedClient.getClient().getMods())
			if(m.enabled)
				m.toggle();
	}

}
