package de.chrissx.mods.fun;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Commandable;
import de.chrissx.util.Util;

public class KillPotion implements Commandable, Bindable {

	@Override
	public void processCommand(String[] args) {
		Util.cheatItem(Util.getCustomPotion(Util.addEffect(Util.newEffects(), 6, 125, 2000), "Killer Potion of Death"), 36);
	}

	@Override
	public void onHotkey() {
		processCommand(null);
	}

	@Override
	public String getName() {
		return "KillPotion";
	}
}