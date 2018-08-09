package de.chrissx.mods.building;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BedFucker extends Mod {

	byte range = 6;
	File rf;
	
	public BedFucker() {
		super("BedFucker");
		rf = getApiFile("range");
	}

	@Override
	public void onTick() {
		if(enabled) {
			BlockPos[] bps = Util.getBlocksAround(mc.thePlayer, range, false);
			for(BlockPos bp : bps)
				if(mc.theWorld.getBlock(bp).getUnlocalizedName().equals("tile.bed")) {
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, bp, EnumFacing.UP));
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, bp, EnumFacing.UP));
				}
		}
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("range"))
			try {
				range = Byte.parseByte(args[2]);
			} catch (Exception e) {
				Util.sendMessage("Error parsing byte.");
			}
		else
			Util.sendMessage("#bedfucker to toggle, #bedfucker range <int> to set the range.");
	}

	@Override
	public void apiUpdate() {
		write(rf, range);
	}
}
