package de.chrissx.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class EntityFakePlayer extends EntityOtherPlayerMP {

	Minecraft mc = Minecraft.getMinecraft();

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
		mc.thePlayer.setPositionAndRotation(posX, posY, posZ, rotationYaw, rotationPitch);
		mc.theWorld.removeEntityFromWorld(getEntityId());
	}
}