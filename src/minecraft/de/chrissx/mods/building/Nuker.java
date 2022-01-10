package de.chrissx.mods.building;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.util.Random;
import de.chrissx.util.Util;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class Nuker extends Mod {

	int count = 6;
	NukerBypassLevel bypass = NukerBypassLevel.NONE;
	NukerMode mode = NukerMode.ALL;
	File bpf, mf;

	public Nuker() {
		super("Nuker", "nuker", "Breaks all blocks around you");
		bpf = getApiFile("bypass");
		mf = getApiFile("mode");
	}

	int breaksPerCycle() {
		return bypass == NukerBypassLevel.SLOW ? 3 : 1;
	}

	@Override
	public void onTick() {
		if (bypass == NukerBypassLevel.NONE || count < 1) {
			BlockPos[] positions = Util.getBlocksAround(player(), (bypass == NukerBypassLevel.NONE ? 6 : 3),
			                                            bypass != NukerBypassLevel.NONE);
			if (mode.equals(NukerMode.ALL)) {
				if (bypass == NukerBypassLevel.NONE) {
					for (BlockPos p : positions)
						Util.breakBlock(p);
				} else {
					if (positions.length <= breaksPerCycle()) {
						for (BlockPos p : positions)
							Util.breakBlock(p);
					} else {
						List<Integer> sent = new ArrayList<Integer>();
						for (int i = 0; i < breaksPerCycle(); i++) {
							int rr = Random.rand(positions.length - 1);
							while (sent.contains(rr))
								rr = Random.rand(positions.length - 1);
							if (bypass == NukerBypassLevel.LEGIT)
								Util.faceBlock(positions[rr]);
							Util.breakBlock(positions[rr]);
							sent.add(rr);
						}
					}
				}
			} else if (mode.equals(NukerMode.CLICK)) {
				BlockPos b = playerController().clickedBlock;
				if (b == null)
					return;
				if (bypass == NukerBypassLevel.NONE) {
					for (BlockPos p : positions)
						if (Block.getIdFromBlock(world().getBlock(p)) == Block.getIdFromBlock(world().getBlock(b)))
							Util.breakBlock(p);
				} else {
					List<BlockPos> poss = new ArrayList<BlockPos>();
					for (BlockPos p : positions)
						if (Block.getIdFromBlock(world().getBlock(p)) == Block.getIdFromBlock(world().getBlock(b)))
							poss.add(p);

					if (poss.size() <= breaksPerCycle())
						for (BlockPos p : poss)
							Util.breakBlock(p);
					else {
						List<Integer> sent = new ArrayList<Integer>();
						for (int i = 0; i < breaksPerCycle(); i++) {
							int rr = Random.rand(poss.size() - 1);
							while (sent.contains(rr))
								rr = Random.rand(poss.size() - 1);
							if (bypass == NukerBypassLevel.LEGIT)
								Util.faceBlock(poss.get(rr));
							Util.breakBlock(poss.get(rr));
							sent.add(rr);
						}
					}
				}
			} else
				Util.sendMessage(
				    "\u00a74I guess I f*cked up and forgot to add support for this mode, please report this!");
			count = 6;
		} else
			count--;
	}

	@Override
	public String getRenderstring() {
		return name + "(BYPASS:" + bypass + ",MODE:" + mode + ")";
	}

	// TODO: remove and use standard options
	@Override
	public void processCommand(String[] args) {
		if (args.length == 1)
			toggle();
		else if (args[1].equalsIgnoreCase("bypass"))
			bypass = bypass == NukerBypassLevel.NONE ? NukerBypassLevel.SLOW
			         : bypass == NukerBypassLevel.SLOW ? NukerBypassLevel.LEGIT : NukerBypassLevel.NONE;
		else if (args[1].equalsIgnoreCase("mode"))
			try {
				mode = NukerMode.valueOf(args[2].toUpperCase());
			} catch (Exception e) {
				Util.sendMessage("\u00a74Error valueOf-ing NukerMode.");
			} else
			Util.sendMessage(
			    "#nuker to toggle, #nuker bypass to toggle bypasses, #nuker mode [ALL/CLICK] to set the mode");
	}

	@Override
	public void apiUpdate() {
		write(bpf, bypass.b);
		write(mf, mode.b);
	}
}
