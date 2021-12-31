package de.chrissx.mods.options;

public class FloatOption extends Option<Float> {

	public FloatOption(String name, String description, float defaultValue) {
		super(name, description, defaultValue);
	}

	@Override
	public void set(String value) {
		this.value = Float.parseFloat(value);
	}

}
