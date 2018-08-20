package de.chrissx.mods;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Mod {

	public FastEat() {
		super("FastEat");
	}
	
	@Override
	public void onTick()
	{
		EntityPlayerSP p = mc.thePlayer;
		if(enabled && p.getHealth() > 0 && p.onGround && mc.gameSettings.keyBindUseItem.isKeyDown() && p.getFoodStats().needFood() && p.getHeldItem().getItem() instanceof ItemFood)
	        for(int i = 0; i < 100; i++)
	        	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
	}

}
