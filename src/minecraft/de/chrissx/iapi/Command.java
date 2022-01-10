package de.chrissx.iapi;

import java.util.function.Consumer;

public class Command {

	public final String cmd;
	public final Consumer<String[]> handler;

	public Command(String cmd, Consumer<String[]> handler) {
		this.cmd = cmd;
		this.handler = handler;
	}
}
