package de.chrissx.mods.building;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;

public class FastBreak extends Mod {
	
	public float speed = 2;
	
	public FastBreak() {
		super("FastBreak");
	}
	
	@Override
	public void processCommand(String[] args)
	{
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("speed"))
			try
			{
				speed = Float.parseFloat(args[2]);
			}
			catch(Exception e)
			{
				Util.sendMessage("Cannot parse speed: " + e.getMessage());
			}
		else
			Util.sendMessage("#fastbreak to toggle, #fastbreak speed <float> to set the divisor");
	}
}