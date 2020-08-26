package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class FastFall extends Mod {

	double speed = -4;
	File sf;
	
	public FastFall() {
		super("FastFall");
		sf = getApiFile("speed");
	}

	@Override
	public void onTick()
	{
		if(enabled && !player().onGround && player().motionY < 0)
			player().motionY = speed;
	}
	
	@Override
	public void processCommand(String[] args)
	{
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("speed"))
			try
			{
				double d = Double.parseDouble(args[2]);
				if(d > 0) throw new Exception("Speed must be negative.");
				speed = d;
			}
			catch(Exception e)
			{
				Util.sendMessage("Error parsing speed: " + e.getMessage());
			}
		else
			Util.sendMessage("#fastfall to toggle, #fastfall speed <negative double> to set the factor");
	}
	
	@Override
	public void apiUpdate()
	{
		write(sf, speed);
	}
}
