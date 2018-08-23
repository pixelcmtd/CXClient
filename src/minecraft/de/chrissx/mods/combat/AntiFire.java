package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiFire extends Mod {

	public AntiFire() {
		super("AntiFire");
	}

	@Override
	public void onTick()
	{
		if(enabled && mc.thePlayer.isBurning() && !mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.onGround)
			for(int i = 0; i < 100; i++)
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
	}
}
