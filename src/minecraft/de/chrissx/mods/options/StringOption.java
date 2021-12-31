package de.chrissx.mods.options;

public class StringOption extends Option<String> {

	public StringOption(String name, String description, String defaultValue) {
		super(name, description, defaultValue);
	}

	@Override
	public void set(String value) {
		this.value = value;
	}

}
