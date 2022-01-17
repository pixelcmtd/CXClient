package de.chrissx.mods.building;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.IntOption;
import de.chrissx.util.Util;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BedFucker extends Mod {

	IntOption range = new IntOption("range", "The range in which to fuck beds", 6);

	public BedFucker() {
		super("BedFucker", "bedfucker", "Automatically fucks beds around you");
		addOption(range);
	}

	@Override
	public void onTick() {
		BlockPos[] bps = Util.getBlocksAround(player(), range.value, false);
		for (final BlockPos bp : bps)
			if (world().getBlock(bp).getUnlocalizedName().equals("tile.bed"))
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
}
