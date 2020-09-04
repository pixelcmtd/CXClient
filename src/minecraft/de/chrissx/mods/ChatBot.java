package de.chrissx.mods;

import net.minecraft.util.IChatComponent;

public interface ChatBot {

	public void onChatMessage(IChatComponent component);
	public String getName();
}
