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

	short count = 21;
	boolean bypass = false;
	NukerMode mode = NukerMode.ALL;
	File bpf, mf;
	
	public Nuker() {
		super("Nuker");
		bpf = getApiFile("bypass");
		mf = getApiFile("mode");
	}

	@Override
	public void onTick() {
		if(enabled) {
			if(!bypass || count < 1) {
				BlockPos[] positions = Util.getBlocksAround(mc.thePlayer, (byte) (bypass ? 3 : 6), bypass);
				if(mode.equals(NukerMode.ALL)) {
					if(!bypass) {
						for(BlockPos p : positions)
							Util.breakBlock(p);
					}else {
						if(positions.length <= 11) {
							for(BlockPos p : positions)
								Util.breakBlock(p);
						}else {
							List<Integer> sent = new ArrayList<Integer>();
							for(int i = 0; i < 11; i++) {
								int rr = Random.rand(positions.length-1);
								while(sent.contains(rr))
									rr = Random.rand(positions.length-1);
								Util.breakBlock(positions[rr]);
								sent.add(rr);
							}
						}
					}
				}else if(mode.equals(NukerMode.CLICK)) {
					BlockPos b = mc.playerController.clickedBlock;
					if(b == null) return;
					if(!bypass) {
						for(BlockPos p : positions)
							if(Block.getIdFromBlock(mc.theWorld.getBlock(p)) == Block.getIdFromBlock(mc.theWorld.getBlock(b)))
								Util.breakBlock(p);
					}else {
						List<BlockPos> poss = new ArrayList<BlockPos>();
						for(BlockPos p : positions)
							if(Block.getIdFromBlock(mc.theWorld.getBlock(p)) == Block.getIdFromBlock(mc.theWorld.getBlock(b)))
								poss.add(p);
						
						if(poss.size() <= 11)
							for(BlockPos p : poss)
								Util.breakBlock(p);
						else {
							List<Integer> sent = new ArrayList<Integer>();
							for(int i = 0; i < 11; i++) {
								int rr = Random.rand(poss.size()-1);
								while(sent.contains(rr))
									rr = Random.rand(poss.size()-1);
								Util.breakBlock(poss.get(rr));
								sent.add(rr);
							}
						}
					}
				} else Util.sendMessage("\u00a74I guess I f*cked up and forgot to add support for this mode, please report this!");
				count = 21;
			} else count--;
		}
	}

	@Override
	public String getRenderstring() {
		return bypass ? name + "(BYPASS,MODE:" + mode + ")" : name + "(MODE:" + mode + ")";
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else
			if(args[1].equalsIgnoreCase("bypass"))
				bypass = !bypass;
			else if(args[1].equalsIgnoreCase("mode"))
				try {
					mode = NukerMode.valueOf(args[2].toUpperCase());
				}catch (Exception e) {
					Util.sendMessage("\u00a74Error valueOf-ing NukerMode.");
				}
			else Util.sendMessage("#nuker to toggle, #nuker bypass to toggle NCP-bypass, #nuker mode [ALL/CLICK] to set the mode");
	}

	@Override
	public void apiUpdate() {
		write(bpf, bypass);
		write(mf, mode.b);
	}
}
