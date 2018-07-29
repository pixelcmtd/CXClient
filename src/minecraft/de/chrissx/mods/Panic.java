package de.chrissx.mods;

import de.chrissx.HackedClient;

public class Panic implements Bindable, Commandable {

	@Override
	public void onHotkey() {
		for(Mod m : HackedClient.getClient().getMods())
			if(m.enabled)
				m.toggle();
	}

	@Override
	public String getName() {
		return "Panic";
	}

	@Override
	public void processCommand(String[] args) {
		onHotkey();
	}

}
