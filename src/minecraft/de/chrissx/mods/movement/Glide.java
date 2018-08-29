package de.chrissx.mods.movement;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Glide extends Mod {

	double speed = -0.05;

	public Glide() {
		super("Glide");
	}

	@Override
	public void onTick()
	{
		EntityPlayerSP p = mc.thePlayer;
		if(enabled && p.isAirBorne && !p.isInWater() && !p.isInLava() && !p.isOnLadder() && p.motionY < 0.0)
	        p.motionY = speed;
	}

	@Override
	public void processCommand(String[] args)
	{
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("speed"))
			try {
				double d = Double.parseDouble(args[2]);
				if(d > 0)
					throw new Exception("The speed must be negative!");
				speed = d;
			} catch (Exception e) {
				Util.sendMessage("Error parsing double: " + e.getMessage());
			}
		else
			Util.sendMessage("#glide to toggle, #glide speed <double> to set the falling speed");
	}
}