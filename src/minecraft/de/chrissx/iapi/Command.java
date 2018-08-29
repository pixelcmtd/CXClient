package de.chrissx.iapi;

import java.util.function.Consumer;

public class Command {

	public final String arg0;
	public final Consumer<String[]> handler;

	public Command(String arg0, Consumer<String[]> handler)
	{
		this.arg0 = arg0;
		this.handler = handler;
	}
}
