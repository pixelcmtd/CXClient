package de.chrissx.mods.fun;

import de.chrissx.mods.Semimod;
import de.chrissx.util.Util;

public class KillPotion extends Semimod {

	public KillPotion() {
		super("KillPotion", "killpotion",
				"Gives you a \"Killer Potion of Death\" that instantly kills everyone and everything");
	}

	@Override
	public void toggle() {
		Util.cheatItem(Util.getCustomPotion(Util.addEffect(Util.newEffects(), 6, 125, 2000), "Killer Potion of Death"),
				36);
	}
}