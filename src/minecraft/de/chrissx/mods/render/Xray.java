package de.chrissx.mods.render;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;

public class Xray extends Mod {

	float gammaBefore = 0;
	public List<Integer> xrayBlocks = new ArrayList<Integer>();

	public Xray() {
		super("XRay", "Makes most or all blocks invisible");
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		GameSettings gs = settings();
		gammaBefore = gs.gammaSetting;
		gs.gammaSetting = enabled ? 100 : gammaBefore;
		mc.renderGlobal.loadRenderers();
	}

	// TODO: here we also need some kind of list option
	@Override
	public void processCommand(String[] args) {
		if (args.length > 1 && args[0].equalsIgnoreCase("add"))
			xrayBlocks.add(Block.getIdFromBlock(world().getBlock(playerController().clickedBlock)));
		else if (args.length > 1 && args[0].equalsIgnoreCase("remove"))
			xrayBlocks.remove(Block.getIdFromBlock(world().getBlock(playerController().clickedBlock)));
		else
			super.processCommand(args);
	}
}
