package de.chrissx.mods.combat;

public enum AimbotMode {
GUN((byte)0), BOW((byte)1);

final byte b;

AimbotMode(byte b)
{
	this.b = b;
}
}
