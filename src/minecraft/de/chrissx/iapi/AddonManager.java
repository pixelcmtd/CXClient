package de.chrissx.iapi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.chrissx.Util;
import net.minecraft.client.Minecraft;

public class AddonManager {
	
	List<CommandExecutor> cmdExecs = new ArrayList<CommandExecutor>();
	
	Map<String, Addon> addons = new HashMap<String, Addon>();
	
	final String path;
	
	final Minecraft mc = Minecraft.getMinecraft();
	
	public AddonManager(String addonPath) {
		path = addonPath;
		
		for(File f : new File(path).listFiles())
			try {
//				ClassLoader cl = URLClassLoader.newInstance(new URL[] {f.toURI().toURL()});
//				AddonProperties props = loadProperties(f);
//				Addon addon = (Addon) cl.loadClass(props.getMainClass()).newInstance();
//				addAddon(addon);
			} catch (Exception e) {
				mc.logger.fatal("Failed to load addon " + f);
				mc.logger.fatal(e);
			}
	}
	
	/**
	 * Loads the addon's properties.
	 * @param f The file that should contain the addon
	 * @return The loaded props
	 * @throws ZipException
	 * @throws IOException
	 */
	AddonProperties loadProperties(File f) throws ZipException, IOException {
		AddonProperties props = new AddonProperties();
		ZipFile zip = new ZipFile(f);
		byte[] bfr = new byte[1024*1024]; //yea this means the c.addon file cannot be larger than 1 MiB
		int size = zip.getInputStream(zip.getEntry("c.addon")).read(bfr);
		String rawData = new String(bfr, 0, size, StandardCharsets.UTF_8);
		zip.close();
		for(String s : rawData.replace("\r", "").split("\n")) {
			String[] tokens = s.split(" ");
			if(tokens[0].equalsIgnoreCase("name"))
				props.setName(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("author"))
				props.setAuthor(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("version"))
				props.setVersion(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("desc"))
				props.setDesc(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("main"))
				props.setMainClass(tokens[1]);
		}
		return props;
	}
	
	public Addon getAddon(String name) {
		return addons.get(name.toLowerCase());
	}
	
	public void addAddon(Addon a) {
		addons.put(a.getName().toLowerCase(), a);
	}
	
	public void registerCommandExecutor(CommandExecutor cmdExec) {
		cmdExecs.add(cmdExec);
	}
	
	/**
	 * tries to execute the command with every registered commandexecutor
	 * @param args the args of the processCommand
	 * @return true if any commandexecutor was able to execute the command, false if not
	 */
	public boolean execCmd(String[] args)
	{
		for(CommandExecutor ce : cmdExecs)
			if(ce.onCommand(args))
				return true;
		return false;
	}
}
