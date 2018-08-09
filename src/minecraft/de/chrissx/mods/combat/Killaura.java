package de.chrissx.mods.combat;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class Killaura extends Mod {

	double max_range = 6;
	KillauraMode mode = KillauraMode.BOTH;
	boolean attackInvis = false;
	boolean legit = false;
	List<String> whitelistedPlayers = new ArrayList<String>();
	File rf, mf, aif, lf;
	
	public Killaura() {
		super("KillAura");
		rf = getApiFile("range");
		mf = getApiFile("mode");
		aif = getApiFile("attack_invis");
		lf = getApiFile("legit");
	}

	@Override
	public void onTick() {
		if(enabled) {
			for(Entity e : mc.theWorld.loadedEntityList) {
				if(!(e instanceof EntityLivingBase) || e == mc.thePlayer || mc.thePlayer.getDistanceToEntity(e) > max_range || (!attackInvis && e.isInvisible()) || e.isDead || (legit && mc.thePlayer.isEating()))
					continue;
				else {
					boolean attack = Util.rand(3) == 2;
					boolean miss = (Util.randBool() && Util.randBool());
					
					if(legit && attack && !miss)
						Util.faceEntity(e);
					if(!hc.getMods().noswing.isEnabled() && (attack || !legit))
						mc.thePlayer.swingItem();
					if((attack && !miss) || !legit)
						mc.playerController.attackEntity(mc.thePlayer, e);
					if(legit)
						return;
				}
			}
		}
	}
	
	@Override
	public boolean onRender(FontRenderer r, int x, int y) {
		if(enabled)
			r.drawString(name+"(RANGE:" + max_range + ",MODE:" + mode.toString() + ",INVIS:" + (attackInvis ? "YA" : "NA") + ",LEGIT:" + (legit ? "YA" : "NA") + ")", x, y, Color.WHITE.getRGB());
		return enabled;
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1) {
			toggle();
		}else {
			if(args[1].equalsIgnoreCase("range"))
				try {
					max_range = Double.parseDouble(args[2]);
				}catch(Exception e) {
					Util.sendMessage("�4Error parsing double.");
				}
			else if(args[1].equalsIgnoreCase("mode"))
				try {
					mode = KillauraMode.valueOf(args[2].toUpperCase());
				} catch (Exception e) {
					Util.sendMessage("�4Error valueOf-ing KillauraMode.");
				}
			else if(args[1].equalsIgnoreCase("invis"))
				attackInvis = !attackInvis;
			else if(args[1].equalsIgnoreCase("add"))
				whitelistedPlayers.add(args[2]);
			else if(args[1].equalsIgnoreCase("remove"))
				whitelistedPlayers.remove(args[2]);
			else if(args[1].equalsIgnoreCase("legit"))
				legit = !legit;
			else
				Util.sendMessage("#killaura to toggle, #killaura range <double> to set range, #killaura mode <KillauraMode> to set mode, #killaura invis to toggle invis-attacking, #killaura add <String> to add whitelisted player, #killaura remove to remove whitelisted player, #killaura legit to toggle targetting.");
		}
	}

	@Override
	public void apiUpdate() {
		write(rf, max_range);
		write(mf, mode.b);
		write(aif, attackInvis);
		write(lf, legit);
	}
}