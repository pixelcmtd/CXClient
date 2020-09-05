package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiFire extends Mod {

	public AntiFire() {
		super("AntiFire", "antifire");
	}

	@Override
	public void onTick()
	{
		if(player().isBurning() && !player().capabilities.isCreativeMode && player().onGround)
			for(int i = 0; i < 100; i++)
				sendPacket(new C03PacketPlayer());
	}
}
