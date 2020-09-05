package de.chrissx.mods;

import java.io.File;

import de.chrissx.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Mod {

	File sf;
	int speed = 100;

	public FastEat() {
		super("FastEat", "fasteat");
		sf = getApiFile("speed");
	}

	@Override
	public void onTick()
	{
		EntityPlayerSP p = player();
		ItemStack is;
		try
		{
			if(enabled
					&& p.getHealth() > 0
					&& p.onGround
					&& settings().keyBindUseItem.isKeyDown()
					&& p.getFoodStats().needFood()
					&& (is = p.getHeldItem()) != null
					&& is.getItem() instanceof ItemFood)
		        for(int i = 0; i < speed; i++)
		        	sendPacket(new C03PacketPlayer(false));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
			Util.sendMessage("#fasteat to toggle, #fasteat speed <int> to set packets per tick (default is 100)");
	}

	@Override
	public void apiUpdate()
	{
		write(sf, speed);
	}

}
