package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;

public class FastFall extends Mod {

	public double factor = -4;
	
	public FastFall() {
		super("FastFall");
	}

	@Override
	public void onTick()
	{
		if(enabled && !mc.thePlayer.onGround && mc.thePlayer.motionY < 0)
		{
			mc.thePlayer.motionY = factor;
		}
	}
	
	@Override
	public void processCommand(String[] args)
	{
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("factor"))
			try
			{
				double d = Double.parseDouble(args[2]);
				if(d > 0)
					throw new Exception("Factor should be negative.");
				else
					factor = d;
			}
			catch(Exception e)
			{
				Util.sendMessage("Error parsing factor: " + e.getMessage());
			}
		else
			Util.sendMessage("#fastfall to toggle, #fastfall factor <negative double> to set the factor");
	}
}
