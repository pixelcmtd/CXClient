package de.chrissx.mods.options;

public class BooleanOption extends Option<Boolean> {

	public BooleanOption(String name, String description, boolean defaultValue) {
		super(name, description, defaultValue);
	}

	@Override
	public void set(String value) {
		// TODO: check if Boolean.parseBoolean is as liberal as systemd
		this.value = value.length() == 0 ? !this.value : Boolean.parseBoolean(value);
	}

}
