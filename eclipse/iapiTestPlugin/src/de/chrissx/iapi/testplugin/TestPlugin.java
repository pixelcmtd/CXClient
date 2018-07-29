package de.chrissx.iapi.testplugin;

import de.chrissx.Util;
import de.chrissx.iapi.Addon;
import de.chrissx.iapi.CommandExecutor;

public class TestPlugin extends Addon implements CommandExecutor {

	public TestPlugin() {
		super("TestPlugin");
		getManager().registerCommandExecutor(this);
	}

	@Override
	public boolean onCommand(String[] args) {
		if(args[0].equalsIgnoreCase("#ping")) {
			Util.sendMessage("Pong!");
			return true;
		}
		return false;
	}

}
