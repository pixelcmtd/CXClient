package de.chrissx.mods.building;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BedFucker extends Mod {

	int range = 6;
	File rf;
	
	public BedFucker() {
		super("BedFucker", "bedfucker");
		rf = getApiFile("range");
	}

	@Override
	public void onTick() {
		BlockPos[] bps = Util.getBlocksAround(player(), range, false);
		for(final BlockPos bp : bps)
			if(world().getBlock(bp).getUnlocalizedName().equals("tile.bed"))
				new Thread(new Runnable() {
					public void run() {
						sendPacket(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, bp, EnumFacing.UP));
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						sendPacket(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, bp, EnumFacing.UP));
					}
				}).start();
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("range"))
			try {
				range = Integer.parseInt(args[2]);
			} catch (Exception e) {
				Util.sendMessage("Error parsing range.");
			}
		else
			Util.sendMessage("#bedfucker to toggle, #bedfucker range <int> to set the range.");
	}

	@Override
	public void apiUpdate() {
		write(rf, range);
	}
}
