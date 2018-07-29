package de.chrissx.mods.chat;

import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;

public class Home implements Bindable {

	@Override
	public void onHotkey() {
		Util.sendChat("/home");
	}
	
	@Override
	public String getName() {
		return "Home";
	}
}