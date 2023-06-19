package de.chrissx.util;

import java.io.File;
import java.nio.file.Paths;

public class Consts {
	public static void init(File dotMinecraftDir) {
		dotMinecraftPath = dotMinecraftDir.getAbsolutePath();
		cxclientPath = Paths.get(dotMinecraftPath, "cxclient").toString();
		addonPath = Paths.get(cxclientPath, "addons").toString();
		hotkeyFile = Paths.get(cxclientPath, "hotkeys.cfg").toString();
	}
	public static final int[] packetPlayerInventorySlots = new int[] {36, 37, 38, 39, 40, 41, 42, 43, 44,
	                                                                  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
	                                                                 };
	public static final int[] localPlayerInventorySlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
	                                                                 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
	                                                                };
	public static String dotMinecraftPath;
	public static String cxclientPath;
	public static String addonPath;
	public static String hotkeyFile;
	public static final String clientName = "CXClient";
	public static final String prefix = "\u00a7a\u00a7l[c] \u00a7f";
	public static final String version = "3.3";
	public static final String mcVersion = "1.8.8";
	public static final int BLDNUM = 160;
	public static final int APIVER = -1;
	public static final String[] changelog = new String[] {
	    clientName + " " + version + " Changelog:",
	    "",
	    "— Improvements to the help messages of most commands (and a general rework of how most work)",
	    "— Removed all of the Twitch streaming and Realms support from Minecraft",
	    "— Added a TriggerBot",
	    "— Fixed the IAUI overlapping with the debug info (F3)",
	};

	public static final String[] credits = new String[] {
	    clientName + " " + version + " Credits:",
	    "",
	    "Author: pixel",
	    "Released by: chrissx Media",
	    "Licensed under: BSD 3-clause",
	    "Official website (alpha): https://pixelcmtd.github.io/CXClient/",
	    "Source code: https://github.com/pixelcmtd/CXClient",
	    "",
	    "Thanks to:",
	    "",
	    "— Garkolym for showing a few exploits in his videos (for example #text)",
	    "— The developers of Wurst for making another open source client, we looked at, when we needed ideas for hacks or when we just f*ed up",
	    "— Trace (german hacking youtuber, quit around 01/2018) for showing a few exploits in his videos: https://youtube.com/c/Trace1337",
	    "— A few other people we stole the Fly- and Speed-Bypasses from"
	};
}
