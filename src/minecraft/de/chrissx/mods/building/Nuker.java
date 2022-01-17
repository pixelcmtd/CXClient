package de.chrissx.mods.building;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.EnumOption;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Random;
import de.chrissx.util.Util;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class Nuker extends Mod {

	int count = 6;
	Option<NukerBypassLevel> bypass = new EnumOption<NukerBypassLevel>(NukerBypassLevel.class, "bypass", "None, slow, or legit, how hard to bypass AC", new NukerBypassLevel[] {NukerBypassLevel.NONE, NukerBypassLevel.SLOW, NukerBypassLevel.LEGIT});
	Option<NukerMode> mode = new EnumOption<NukerMode>(NukerMode.class, "mode", "All or click, which blocks to break", new NukerMode[] {NukerMode.ALL, NukerMode.CLICK});

	public Nuker() {
		super("Nuker", "nuker", "Breaks all blocks around you");
		addOption(bypass);
		addOption(mode);
	}

	int breaksPerCycle() {
		return bypass.value == NukerBypassLevel.SLOW ? 3 : 1;
	}

	@Override
	public void onTick() {
		if (bypass.value == NukerBypassLevel.NONE || count < 1) {
			BlockPos[] positions = Util.getBlocksAround(player(), (bypass.value == NukerBypassLevel.NONE ? 6 : 3),
			                                            bypass.value != NukerBypassLevel.NONE);
			if (mode.value == NukerMode.ALL) {
				if (bypass.value == NukerBypassLevel.NONE) {
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
							if (bypass.value == NukerBypassLevel.LEGIT)
								Util.faceBlock(positions[rr]);
							Util.breakBlock(positions[rr]);
							sent.add(rr);
						}
					}
				}
			} else if (mode.value == NukerMode.CLICK) {
				BlockPos b = playerController().clickedBlock;
				if (b == null)
					return;
				if (bypass.value == NukerBypassLevel.NONE) {
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
							if (bypass.value == NukerBypassLevel.LEGIT)
								Util.faceBlock(poss.get(rr));
							Util.breakBlock(poss.get(rr));
							sent.add(rr);
						}
					}
				}
			} else
				Util.sendError(
				    "I guess I f*cked up and forgot to add support for this mode, please report this!");
			count = 6;
		} else
			count--;
	}

	@Override
	public String getRenderstring() {
		return name + "(BYPASS:" + bypass + ",MODE:" + mode + ")";
	}
}
