package de.chrissx.mods.fun;

import de.chrissx.mods.Semimod;
import de.chrissx.util.Util;

public class Throw extends Semimod {

	public Throw() {
		super("Throw", "Throws items, many, quickly");
	}

	long throwCount = 500;
	long delay = 1;

	@Override
	public void processCommand(String[] args) {
		if (args.length < 2)
			Util.sendError("Usage: throw <count> <delay>");
		else {
			try {
				throwCount = Long.parseLong(args[0]);
				delay = Long.parseLong(args[1]);
			} catch (Exception e) {
				Util.sendError("Error parsing count and delays. (longs)");
			}
			toggle();
		}
	}

	@Override
	public void toggle() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (long i = 1; i <= throwCount; i++) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					click(false);
					Util.sendMessage(i + "/" + throwCount + " thrown.");
				}
			}
		}).start();
	}
}