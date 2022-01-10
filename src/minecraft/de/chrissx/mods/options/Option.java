package de.chrissx.mods.options;

public abstract class Option<T> {
	public String name, description;
	public boolean parsed = false;
	public T value;
	public T defaultValue;
	// TODO: we might want abbrs
	// TODO: something for saving

	public Option(String name, String description, T defaultValue) {
		this.name = name;
		this.description = description;
		value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public abstract void set(String value);

	@Override
	public String toString() {
		return value.toString();
	}
}
