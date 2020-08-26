package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.BlockPos;

public class JailsmcBot extends Mod {
	
	public JailsmcBot() {
		super("JailsMCBot");
	}

	@Override
	public void onTick() {
		if(enabled) {
			if(inventory().getFirstEmptyStack() != -1) {
				for(BlockPos p : Util.getBlocksAround(player(), 4, false)) {
					Block b = world().getBlock(p);
					if(!(b instanceof BlockObsidian) && !(b instanceof BlockGlowstone) && !(b instanceof BlockBasePressurePlate) && !(b instanceof BlockAir))
						Util.breakBlock(p);
				}
			}
			else
				Util.sendChat("/sellall");
		}
	}
}
