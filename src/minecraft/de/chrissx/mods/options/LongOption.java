package de.chrissx.mods.options;

public class LongOption extends Option<Long> {

	public LongOption(String name, String description, long defaultValue) {
		super(name, description, defaultValue);
	}

	@Override
	public void set(String value) {
		this.value = Long.parseLong(value);
	}

}
