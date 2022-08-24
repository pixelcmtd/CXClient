package de.chrissx.mods;

// TODO: get rid of this in favor of inheriting from Command
public interface CommandExecutor {
	public void processCommand(String[] args);
	public String argv0();
}
