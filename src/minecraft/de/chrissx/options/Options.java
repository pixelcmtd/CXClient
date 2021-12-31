package de.chrissx.options;

import java.io.File;

import de.chrissx.EapiOptions;
import de.chrissx.util.Util;

// FIXME: holy fuck what did i do
public class Options implements IOptions {

	public EapiOptions eapi;

	public Options() {
		eapi = new EapiOptions();
	}

	@Override
	public void init(File config) {
	}

	@Override
	public void set(String[] args) {
		if (args.length > 1 && args[1].equalsIgnoreCase("eapi"))
			eapi.set(args);
		else
			list(args);
	}

	@Override
	public void get(String[] args) {
		if (args.length > 1 && args[1].equalsIgnoreCase("eapi"))
			eapi.get(args);
		else
			list(args);
	}

	@Override
	public void list(String[] args) {
		if (args.length > 1 && args[1].equalsIgnoreCase("eapi"))
			eapi.list(args);
		else
			Util.sendMessage("Categories: eapi");
	}

	@Override
	public void stop(File config) {
	}
}
