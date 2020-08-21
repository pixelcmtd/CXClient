package de.chrissx.mods.building;

public enum NukerBypassLevel {

	NONE(0),
	SLOW(1),
	LEGIT(2);

	//this is only a byte for backward compatibility reasons,
	//so you don't have to re-write your eAPI using software
	final byte b;

	NukerBypassLevel(int b)
	{
		this.b = (byte)b;
	}
}
