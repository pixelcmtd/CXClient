package de.chrissx.mods.render;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;

public class Xray extends Mod {

	float gammaBefore = 0;
	public List<Integer> xrayBlocks = new ArrayList<Integer>();
	
	public Xray() {
		super("XRay");
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		GameSettings gs = settings();
		gammaBefore = gs.gammaSetting;
		gs.gammaSetting = enabled ? 100 : gammaBefore;
		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("add"))
			xrayBlocks.add(Block.getIdFromBlock(world().getBlock(playerController().clickedBlock)));
		else if(args[1].equalsIgnoreCase("remove"))
			xrayBlocks.remove(Block.getIdFromBlock(world().getBlock(playerController().clickedBlock)));
		else
			Util.sendMessage("#xray to toggle, #xray add to add last clicked block to whitelisted blocks, #xray remove to remove last clicked block from whitelisted blocks.");
	}
}