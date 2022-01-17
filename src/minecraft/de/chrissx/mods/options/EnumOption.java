package de.chrissx.mods.options;

import de.chrissx.util.Util;

public class EnumOption<T extends Enum<T>> extends Option<T> {

	Class<T> E;
	T[] values;

	public EnumOption(Class<T> E, String name, String description, T[] values) {
		super(name, description, values[0]);
		this.E = E;
		this.values = values;
	}

	@Override
	public void set(String value) {
		this.value = value == "" ? values[values[values.length] == this.value ? 0 : Util.indexOf(values, this.value) + 1]
		             : T.valueOf(E, value.toUpperCase());
	}

}
