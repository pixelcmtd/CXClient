package de.chrissx.hotkeys;

import java.util.List;

import de.chrissx.mods.Bindable;

public class Hotkey {

	public int key;
	public Bindable handler;

	public Hotkey(int key, Bindable handler) {
		this.key = key;
		this.handler = handler;
	}

	public boolean equals(Object o) {
		return o instanceof Hotkey ? equals((Hotkey) o) : false;
	}

	public boolean equals(Hotkey hk) {
		return hk.key == key && hk.handler.getName() == handler.getName();
	}

	public static boolean containsKey(List<Hotkey> hks, int key) {
		for (Hotkey hk : hks)
			if (hk.key == key)
				return true;
		return false;
	}
}
