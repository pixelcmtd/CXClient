package de.chrissx.mods.movement;

public enum SneakMode {
PACKET((byte)0), BYPASS((byte)1);
	
	byte b;
	
	SneakMode(byte b)
	{
		this.b = b;
	}
}