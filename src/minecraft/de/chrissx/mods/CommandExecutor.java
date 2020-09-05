package de.chrissx.mods;

public interface CommandExecutor {

	public void processCommand(String[] args);

	public String getArgv0();
	
}
