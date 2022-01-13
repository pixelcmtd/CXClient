package de.chrissx.iapi.testplugin;

import java.util.function.Consumer;

import de.chrissx.iapi.Addon;
import de.chrissx.iapi.Command;
import de.chrissx.util.Util;

public class TestPlugin extends Addon {

	public TestPlugin() {
		super("TestAddon", "pixel, chrissx Media", "1.0.0", "This is a addon/plugin used to test the iAPI capabilities of the CXClient.");
		getManager().registerCommand(new Command("#ping", new Consumer<String[]>() {
			@Override
			public void accept(String[] arg0) {
				Util.sendMessage("Pong!");
			}
		}));
	}
}
