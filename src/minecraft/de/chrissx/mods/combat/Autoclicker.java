package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.mods.StopListener;
import de.chrissx.mods.options.BooleanOption;

// TODO: rethink 20 times why this is not capitalized
public class Autoclicker extends Mod implements StopListener {

	// TODO: i can make this much better
	BooleanOption mouseButton = new BooleanOption("button", "", true);

	public Autoclicker() {
		// TODO:
		super("AutoClicker", "");
		addOption(mouseButton);
	}

	@Override
	public void onTick() {
		click(mouseButton.value);
	}

	@Override
	public String getRenderstring() {
		return name + "(" + (mouseButton.value ? "LEFT" : "RIGHT") + ")";
	}

	// FIXME: why?!
	@Override
	public void onStop() {
	}
}