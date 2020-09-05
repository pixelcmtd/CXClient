package de.chrissx.mods;

import java.io.File;

import de.chrissx.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Mod {

	File sf;
	int speed = 250;

	public Regen() {
		super("Regen", "regen");
		sf = getApiFile("speed");
	}

	@Override
	public void onTick()
	{
		EntityPlayerSP p = player();
		if(enabled && !playerController().isInCreativeMode() && p.getFoodStats().getFoodLevel() > 17 && p.getHealth() < 20 && p.getHealth() != 0 && p.onGround)
			for(short i = 0; i < speed; i++)
				sendPacket(new C03PacketPlayer());
	}

	@Override
	public void processCommand(String[] args)
	{
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("speed"))
			try
			{
				speed = Integer.parseInt(args[2]);
			}
			catch(Exception e)
			{
				Util.sendMessage("\u00a74Error parsing int: " + e.getMessage());
			}
		else
			Util.sendMessage("#regen to toggle, #regen speed <int> to set packets per tick (default is 100)");
	}

	@Override
	public void apiUpdate()
	{
		write(sf, speed);
	}

}
