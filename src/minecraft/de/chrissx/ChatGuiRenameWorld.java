package de.chrissx;

import de.chrissx.util.Util;

public class ChatGuiRenameWorld implements IGuiRenameWorld {

	String buffer;

	public ChatGuiRenameWorld(String s) {
		buffer = s;
	}

	@Override
	public void setText(String s) {
		buffer = s;
		Util.sendMessage(s);
	}

	@Override
	public String getText() {
		return buffer;
	}
}
