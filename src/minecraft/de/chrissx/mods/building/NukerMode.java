package de.chrissx.mods.building;

public enum NukerMode {
ALL((byte)0), CLICK((byte)1);

final byte b;

NukerMode(byte b)
{
	this.b = b;
}
}