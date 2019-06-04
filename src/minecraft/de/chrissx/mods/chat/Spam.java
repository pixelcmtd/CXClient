package de.chrissx.mods.chat;

import de.chrissx.mods.Mod;
import de.chrissx.util.Consts;
import de.chrissx.util.Random;
import de.chrissx.util.Util;

public class Spam extends Mod {
	
	public Spam() {
		super("Spam");
	}
	
	@Override
	public void toggle() {}

	@Override
	public void processCommand(String[] args) {
		if(args.length < 4) {
			Util.sendMessage("\u00a74Not enough arguments, usage: #(clear)spam <times> <delay> <message>");
			return;
		}
		
		final int times;
		final StringBuilder msg = new StringBuilder();
		final long delay;
		final boolean clear;
		
		clear = !args[0].equalsIgnoreCase("#spam");
		
		try {
			times = Integer.parseInt(args[1]);
		}catch(Exception e) {
			Util.sendMessage("\u00a74Error parsing times.");
			return;
		}
		
		try {
			delay = Long.parseLong(args[2]);
		} catch (Exception e) {
			Util.sendMessage("\u00a74Error parsing delay.");
			return;
		}
		
		msg.append(args[3]);
		for(int i = 4; i < args.length; i++)
			msg.append(" " + args[i]);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				enabled = true;
				String last = "-1";
				final String m = msg.toString();
				for(int i = 0; i < times; i++) {
					if(clear)
						Util.sendChat(m);
					else {
						String r = last;
						while(last == r)
							r = Integer.toHexString(Random.rand(0x1000));
						Util.sendChat(m + " :" + r);
						last = r;
					}
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void onHotkey() {
		processCommand(new String[] {"#spam", "20", "50", "Sponsored by " + Consts.clientName + " by chrissx"});
	}
}