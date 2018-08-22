package de.chrissx.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Consts {
	public static final String prefix = "\u00a7c[CXClient] \u00a7f";
	public static final int[] packetPlayerInventorySlots = new int[] {36, 37, 38, 39, 40, 41, 42, 43, 44, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static final int[] localPlayerInventorySlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static final String dotMinecraftPath = new File("").getAbsoluteFile().getAbsolutePath().toString();
	public static final String configPath = Paths.get(dotMinecraftPath, "cxclient_config").toString();
	public static final String addonPath = Paths.get(dotMinecraftPath, "cxclient_addons").toString();
	public static final String eapiPath = Paths.get(dotMinecraftPath, "cxclient_eapi").toString();
	public static final String modsPath = Paths.get(eapiPath, "mods").toString();
	public static final String enabledPath = Paths.get(modsPath, "enabled").toString();
	public static final String togglePath = Paths.get(modsPath, "toggle").toString();
	public static final String tempPath = System.getProperty("java.io.tmpdir");
	//public static final String updaterUrl = "https://chrissxyt.github.io/CXClient/updater/ud.dl";
	public static final String updaterFile = Util.generateTempFile(tempPath, "cxclient_updater", ".jar");
	public static final String runningFile = Paths.get(eapiPath, "running").toString();
	public static final String hotkeyFile = Paths.get(configPath, "hotkeys.cfg").toString();
	public static final String clientName = "CXClient";
	public static final String version = "alpha 2509";
	public static final String[] changelog = new String[] {
			clientName + " " + version + " Changelog:",
			"",
			"-Added a Lag mod that holds your packets back for as long as it's enabled",
			"-Added a ScaffoldWalk that does what a SW does...place blocks",
			"-Added a FastFall that sets your falling speed so you come to ground faster",
			"-Added a FastEat mod that just lets you eat faster",
			"-Fixed hotkeys sometimes not working in SP (the disableHotkeys value was not set to false when loading a world)",
			"-Optimized a few of the Minecraft classes to improve performance a bit",
			"-Added an AutoSwitch mod to cycle through the hotbar ~2 times a second",
			"-Added a speed variable to Regen (and also FastEat)",
			"-Fixed a potential crash on new mod FastEat we encountered when combining FastEat and Regen against 4 Blazes"
	};
	public static final String[] credits = new String[] {
		clientName + " " + version + " Credits:",
		"",
		"Author: chrissx",
		"Released by: chrissx Media Inc.",
		"Licensed under: GNU GPLv3",
		"Official website (alpha): https://chrissxyt.github.io/CXClient/",
		"Source branch: https://github.com/chrissxYT/CXClient/tree/master",
		"",
		"Thanks to:",
		"",
		"-Garkolym for showing a few exploits in his videos (for example #text)",
		"-A few other people we stealt the Fly- and Speed-Bypasses from",
		"-The developers of Wurst for making another open source client, we looked"
		+ " at, when we needed ideas for hacks or when we just f*ed up",
		"-Trace (german hacking youtuber, quit around 01/2018) for showing a few exploits"
		+ "in his videos: https://www.youtube.com/channel/UClgCGHQcdKN7Rwfx1q_-UUw"
	};
	
	public static final String help = "Commands: #text, #multitext, #killpotion, #spam, #clearspam, #twerk, #timer, #speedac1, #speedlegit, "
			+ "#skinblink, #fastplace, #fastbreak, #throw, #tracer, #masstpa, #autoarmor, #say, #bedfucker, #aimbot, #stepjump, #autoswitch, #flip, "
			+ "#cmdblock, #nofall, #fullbright, #panic, #flyvanilla, #flyac1, #flyac2, #trollpotion, #givebypass, #reach, #regen, #fastfall, "
			+ "#xray, #fasthit, #autoclicker, #noswing, #nick, #authmecrack, #antiafk, #give, #velocity, #sprint, #scaffoldwalk, #jetpack, "
			+ "#autosteal, #killaura, #fasteat, #nuker, #sneak, #norender, #changelog, #credits, #bind, #unbind, #binds, #mods, #help";
}
