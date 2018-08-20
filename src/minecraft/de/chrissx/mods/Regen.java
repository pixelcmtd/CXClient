package de.chrissx.mods;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Mod {

	public Regen() {
		super("Regen");
	}
	
	@Override
	public void onTick()
	{
		EntityPlayerSP p = mc.thePlayer;
		if(enabled && !mc.playerController.isInCreativeMode() && p.getFoodStats().getFoodLevel() > 17 && p.getHealth() < 20 && p.getHealth() != 0 && p.onGround)
			for(short i = 0; i < 250; i++)
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
	}

}
