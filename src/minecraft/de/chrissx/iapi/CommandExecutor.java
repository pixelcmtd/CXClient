package de.chrissx.iapi;

public interface CommandExecutor {

	/**
	 * 
	 * @param args The args gotten by splitting the chat input at each " "
	 * @return true if the command was executed, false if it wasn't executed
	 */
	public boolean onCommand(String[] args);

}
