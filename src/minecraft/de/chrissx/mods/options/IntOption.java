package de.chrissx.mods.options;

public class IntOption extends Option<Integer> {

	public IntOption(String name, String description, int defaultValue) {
		super(name, description, defaultValue);
	}

	@Override
	public void set(String value) {
		this.value = Integer.parseInt(value);
	}

}
