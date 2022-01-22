package de.chrissx.mods.chat;

import de.chrissx.mods.ChatBot;
import de.chrissx.mods.Mod;
import net.minecraft.util.IChatComponent;

// TODO: implement
public class RewiWords extends Mod implements ChatBot {

	public RewiWords() {
		super("RewiWords", "rewiwords", "Automatically answers questions in the GetDown mode on the rewinside server");
	}

	@Override
	public void onChatMessage(IChatComponent component) {
		String s = component.getUnformattedText();
		if (s.contains("GetDown") && s.contains("Welches Wort ist gesucht")) {
			System.out.println("Got RewiWords message: " + s);
		}
	}
}
