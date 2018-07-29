package de.chrissx.mods.render;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class Xray extends Mod {

	float gammaBefore = 0;
	public List<Integer> xrayBlocks = new ArrayList<Integer>();
	
	public Xray() {
		super("XRay");
	}
	
	@Override
	public void toggle() {
		enabled = !enabled;
		GameSettings gs = mc.gameSettings;
		if(enabled) {
			gammaBefore = gs.gammaSetting;
			gs.gammaSetting = 100;
		}else {
			gs.gammaSetting = gammaBefore;
		}
		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("add"))
			xrayBlocks.add(Block.getIdFromBlock(mc.theWorld.getBlock(mc.playerController.clickedBlock)));
		else if(args[1].equalsIgnoreCase("remove"))
			xrayBlocks.remove(Block.getIdFromBlock(mc.theWorld.getBlock(mc.playerController.clickedBlock)));
		else
			Util.sendMessage("#xray to toggle, #xray add to add last clicked block to whitelisted blocks, #xray remove to remove last clicked block from whitelisted blocks.");
	}
}