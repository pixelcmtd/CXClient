package de.chrissx.mods;

import java.util.Map;

public abstract class EapiModule {

	protected final String name;

	// TODO: how to remove this?
	public abstract Map<String, Object> apiValues();

	public EapiModule(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
