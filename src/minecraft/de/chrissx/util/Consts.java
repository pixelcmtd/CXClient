package de.chrissx.util;

import java.io.File;
import java.nio.file.Paths;

public class Consts {
	public static final String prefix = "\u00a7c[CXClient] \u00a7f";
	public static final int[] packetPlayerInventorySlots = new int[] {36, 37, 38, 39, 40, 41, 42, 43, 44,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static final int[] localPlayerInventorySlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static final String dotMinecraftPath = new File("").getAbsoluteFile().getAbsolutePath().toString();
	public static final String configPath = Paths.get(dotMinecraftPath, "cxclient_config").toString();
	public static final String addonPath = Paths.get(dotMinecraftPath, "cxclient_addons").toString();
	public static final String eapiPath = Paths.get(dotMinecraftPath, "cxclient_eapi").toString();
	public static final String modsPath = Paths.get(eapiPath, "mods").toString();
	public static final String enabledPath = Paths.get(modsPath, "enabled").toString();
	public static final String togglePath = Paths.get(modsPath, "toggle").toString();
	public static final String runningFile = Paths.get(eapiPath, "running").toString();
	public static final String eapiVersionFile = Paths.get(eapiPath, "eapi_version").toString();
	public static final String mcVersionFile = Paths.get(eapiPath, "mc_version").toString();
	public static final String launchedVersionFile = Paths.get(eapiPath, "launched_version").toString();
	public static final String cxclientVersionFile = Paths.get(eapiPath, "cxclient_version").toString();
	public static final String hotkeyFile = Paths.get(configPath, "hotkeys.cfg").toString();
	public static final String optionsFile = Paths.get(configPath, "options.cfg").toString();
	public static final String eapiOptionsFile = Paths.get(configPath, "eapiOptions.cfg").toString();
	public static final String clientName = "CXClient";
	public static final String version = "alpha 3106";
	public static final String mcVersion = "1.8.8";
	public static final int BLDNUM = -3106;
	public static final int APIVER = -1;
	public static final String[] changelog = new String[] {
		clientName + " " + version + " Changelog:",
		"",
		"-Added more eAPI functionality",
		"-Added an industry-leading WaterWalk/Jesus",
		"-Added a simple AutoJump",
		"-Added an Option system that only controls the eAPI sleep time at the moment",
		"-Added an AuthMeCracker brute-force mode",
	};

	public static final String[] credits = new String[] {
		clientName + " " + version + " Credits:",
		"",
		"Author: chrissx",
		"Released by: chrissx Media",
		"Licensed under: BSD 3-clause",
		"Official website (alpha): https://tinyurl.com/cxclhmpg",
		"Source code: https://tinyurl.com/cxclmstr",
		"",
		"Thanks to:",
		"",
		"-Garkolym for showing a few exploits in his videos (for example #text)",
		"-The developers of Wurst for making another open source client, we looked at, when we needed ideas for hacks or when we just f*ed up",
		"-Trace (german hacking youtuber, quit around 01/2018) for showing a few exploits in his videos: https://tinyurl.com/trcechnl",
		"-A few other people we stealt the Fly- and Speed-Bypasses from"
	};

	public static final String extraHelp = " #cmdblock #bind #mods #unbind #say #binds #give #givebypass #debug #set #get #list #help";
}
