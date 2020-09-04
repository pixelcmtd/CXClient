package de.chrissx.mods.chat;

import de.chrissx.mods.Mod;
import net.minecraft.util.IChatComponent;

public class RewiWords extends Mod {

	public RewiWords() {
		super("RewiWords");
	}

	@Override
	public void onChatMessage(IChatComponent component) {
		if(!enabled) return;
		String s = component.getUnformattedText();
		if(s.contains("GetDown") && s.contains("Welches Wort ist gesucht")); //TODO: implement
	}
}
