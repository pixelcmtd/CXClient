package de.chrissx;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
	public static final String prefx = "§c[CXClient] §f";
	public static final int[] PACKET_PLAYER_INVENTORY_SLOTS = new int[] {36, 37, 38, 39, 40, 41, 42, 43, 44, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static final int[] LOCAL_PLAYER_INVENTORY_SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
	public static final String configPath = Paths.get(new File("").getAbsoluteFile().getAbsolutePath().toString(), "cxclient_config").toString();
	public static final String addonPath = Paths.get(new File("").getAbsoluteFile().getAbsolutePath().toString(), "cxclient_addons").toString();
	public static final String eapiPath = Paths.get(new File("").getAbsoluteFile().getAbsolutePath().toString(), "cxclient_eapi").toString();
	public static final String modsPath = Paths.get(eapiPath, "mods").toString();
	public static final String enabledPath = Paths.get(modsPath, "enabled").toString();
	public static final String togglePath = Paths.get(modsPath, "toggle").toString();
	public static final String tempPath = System.getProperty("java.io.tmpdir");
	public static final String updaterUrl = "https://chrissx.lima-city.de/cxclient/updater.dl";
	public static final String updaterFile = Util.generateTempFile(Constants.tempPath, "cxclient_updater", ".jar");
	public static final String hotkeyFile = Paths.get(Constants.configPath, "hotkeys.cfg").toString();
}
