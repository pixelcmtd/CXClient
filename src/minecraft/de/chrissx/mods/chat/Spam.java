package de.chrissx.mods.chat;

import de.chrissx.HackedClient;
import de.chrissx.Util;
import de.chrissx.mods.Mod;

public class Spam extends Mod {
	
	public Spam() {
		super("Spam");
	}
	
	@Override
	public void toggle() {}

	@Override
	public void processCommand(String[] args) {
		if(args.length < 4) {
			Util.sendMessage("§4Not enough arguments, usage: #(clear)spam <times> <delay> <message>");
			return;
		}
		
		final int times;
		final StringBuilder msg = new StringBuilder();
		final long delay;
		final boolean clear;
		
		if(args[0].equalsIgnoreCase("#spam"))
			clear = false;
		else
			clear = true;
		
		try {
			times = Integer.parseInt(args[1]);
		}catch(Exception e) {
			Util.sendMessage("§4Error parsing times.");
			return;
		}
		
		try {
			delay = Long.parseLong(args[2]);
		} catch (Exception e) {
			Util.sendMessage("§4Error parsing delay.");
			return;
		}
		
		msg.append(args[3]);
		for(int i = 4; i < args.length; i++) {
			msg.append(" "+args[i]);
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				enabled = true;
				short last = -1;
				final String m = msg.toString();
				for(int i = 0; i < times; i++) {
					if(clear)
						Util.sendChat(m);
					else {
						short r = (short)Util.rand(1000);
						while(last == r)
							r = (short)Util.rand(1000);
						Util.sendChat(m+" : "+r);
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
		processCommand(new String[] {"#spam", "20", "50", "Sponsored by " + hc.CLIENT_NAME + " by chrissx"});
	}
}