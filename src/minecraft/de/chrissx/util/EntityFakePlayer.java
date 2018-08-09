package de.chrissx.util;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.World;

public class EntityFakePlayer extends EntityOtherPlayerMP {

	private Minecraft mc = Minecraft.getMinecraft();
	
	public EntityFakePlayer() {
		super(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile());
		copyLocationAndAnglesFrom(mc.thePlayer);
		
		inventory.copyInventory(mc.thePlayer.inventory);
		getDataWatcher().updateObject(10, Byte.valueOf(mc.thePlayer.getDataWatcher().getWatchableObjectByte(10)));
		
		rotationYawHead = mc.thePlayer.rotationYawHead;
		renderYawOffset = mc.thePlayer.renderYawOffset;
		
		chasingPosX = posX;
		chasingPosY = posY;
		chasingPosZ = posZ;
		
		mc.theWorld.addEntityToWorld(getEntityId(), this);
	}
	
	public void destruct() {
		setBackPlayer();
		despawn();
	}
	
	public void setBackPlayer() {
		mc.thePlayer.setPositionAndRotation(posX, posY, posZ, rotationYaw, rotationPitch);
	}
	
	public void despawn() {
		mc.theWorld.removeEntityFromWorld(getEntityId());
	}
}