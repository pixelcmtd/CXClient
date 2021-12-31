package de.chrissx.mods.options;

public class DoubleOption extends Option<Double> {

	public DoubleOption(String name, String description, double defaultValue) {
		super(name, description, defaultValue);
	}

	@Override
	public void set(String value) {
		this.value = Double.parseDouble(value);
	}

}
