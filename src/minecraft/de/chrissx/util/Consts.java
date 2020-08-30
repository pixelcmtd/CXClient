package de.chrissx.util;

import java.io.File;
import java.nio.file.Paths;

public class Consts {
	public static void init(File dotMinecraftDir) {
		dotMinecraftPath = dotMinecraftDir.getAbsolutePath();
		configPath = Paths.get(dotMinecraftPath, "cxclient_config").toString();
		addonPath = Paths.get(dotMinecraftPath, "cxclient_addons").toString();
		eapiPath = Paths.get(dotMinecraftPath, "cxclient_eapi").toString();
		modsPath = Paths.get(eapiPath, "mods").toString();
		enabledPath = Paths.get(modsPath, "enabled").toString();
		togglePath = Paths.get(modsPath, "toggle").toString();
		runningFile = Paths.get(eapiPath, "running").toString();
		eapiVersionFile = Paths.get(eapiPath, "eapi_version").toString();
		mcVersionFile = Paths.get(eapiPath, "mc_version").toString();
		launchedVersionFile = Paths.get(eapiPath, "launched_version").toString();
		cxclientVersionFile = Paths.get(eapiPath, "cxclient_version").toString();
		hotkeyFile = Paths.get(configPath, "hotkeys.cfg").toString();
		optionsFile = Paths.get(configPath, "options.cfg").toString();
		eapiOptionsFile = Paths.get(configPath, "eapiOptions.cfg").toString();
	}
	public static final String prefix = "\u00a7c[CXClient] \u00a7f";
	public static final int[] packetPlayerInventorySlots = new int[] {36, 37, 38, 39, 40, 41, 42, 43, 44,
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static final int[] localPlayerInventorySlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static String dotMinecraftPath;
	public static String configPath;
	public static String addonPath;
	public static String eapiPath;
	public static String modsPath;
	public static String enabledPath;
	public static String togglePath;
	public static String runningFile;
	public static String eapiVersionFile;
	public static String mcVersionFile;
	public static String launchedVersionFile;
	public static String cxclientVersionFile;
	public static String hotkeyFile;
	public static String optionsFile;
	public static String eapiOptionsFile;
	public static final String clientName = "CXClient";
	public static final String version = "alpha 3209";
	public static final String mcVersion = "1.8.8";
	public static final int BLDNUM = -3209;
	public static final int APIVER = -1;
	public static final String[] changelog = new String[] {
		clientName + " " + version + " Changelog:",
		"",
		"-Added an EAPI system for setting homes and tping to them",
		"-Fixed a bug where the client would crash when you didn't click any block and enabled click Nuker",
		"-Added a legit bypass level to Nuker, so you would be almost undetectable",
		"-When (un)binding hotkeys the keys are now matched better",
		"-Dying mobs/players are no longer attacked by the KillAura",
		"-Fixed a bug where when Velocity was enabled other entities (ender pearls) wouldn't move horizontically",
		"-Added a new slowdown mode to slow down Killaura without enabling other legit modes",
		"-Killaura and Aimbot now don't target \"perfectly\", but always look at slightly different points on the target",
		"-A temporary \"#print-players\" command (the current command system is not optimal and will be revised soon, but this one works temporarily)",
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
