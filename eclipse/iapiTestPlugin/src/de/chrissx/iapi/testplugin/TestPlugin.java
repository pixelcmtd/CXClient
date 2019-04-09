package de.chrissx.iapi.testplugin;

import java.util.function.Consumer;

import de.chrissx.iapi.Addon;
import de.chrissx.iapi.Command;
import de.chrissx.util.Util;

public class TestPlugin extends Addon {

	public TestPlugin() {
		getManager().registerCommand(new Command("#ping", new Consumer<String[]>(){@Override
			public void accept(String[] arg0) {Util.sendMessage("Pong!");}}));
	}
}
