package de.chrissx.mods.chat;

import de.chrissx.humanapis.HomeApi;
import de.chrissx.mods.Bindable;

// NOTE: the eAPI part of this has been removed because you can use something like `say home` instead
// TODO: rethink that a bit when im sober again
public class Home implements Bindable {

	@Override
	public void onHotkey() {
		HomeApi.home("");
	}

	@Override
	public String getName() {
		return "Home";
	}
}
