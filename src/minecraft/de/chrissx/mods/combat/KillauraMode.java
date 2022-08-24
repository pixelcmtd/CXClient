package de.chrissx.mods.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

// TODO: rename
public enum KillauraMode {
	PLAYERS((byte)1), MOBS((byte)2), BOTH((byte)0);


	final byte b;

	KillauraMode(byte b) {
		this.b = b;
	}

	public boolean checkEntity(Entity e) {
		if(!(e instanceof EntityLivingBase))
			return false;
		if(this == BOTH)
			return true;
		if(this == PLAYERS && e instanceof EntityPlayer)
			return true;
		if(this == MOBS && e instanceof EntityLivingBase && !(e instanceof EntityPlayer))
			return true;
		return false;
	}
}