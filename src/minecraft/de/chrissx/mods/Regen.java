package de.chrissx.mods;

import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Mod {

	public Regen() {
		super("Regen");
	}
	
	@Override
	public void onTick()
	{
		if(enabled && !mc.playerController.isInCreativeMode() && mc.thePlayer.getFoodStats().getFoodLevel() > 17 && mc.thePlayer.getHealth() < 20 && mc.thePlayer.getHealth() != 0 && mc.thePlayer.onGround)
			for(byte i = 0; i < 250; i++)
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
	}

}
