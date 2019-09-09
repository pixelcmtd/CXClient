package de.chrissx.iapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.chrissx.ChatGuiRenameWorld;
import de.chrissx.HackedClient;
import de.chrissx.mods.ModList;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;

public class AddonManager {

	Map<Addon, AddonProperties> addons = new HashMap<Addon, AddonProperties>();
	List<Command> commands = new ArrayList<Command>();

	/**
	 * the path in which the addons are located
	 */
	String path;

	/**
	 * the minecraft instance
	 */
	final Minecraft mc = Minecraft.getMinecraft();

	/**
	 * initializes the addonmanager and loads the addons from the path
	 * @param addonPath the path where the addons are located
	 */
	public void init(String addonPath)
	{
		path = addonPath;
		
		for(File f : new File(path).listFiles())
			try {
				AddonProperties p = loadProperties(f);
				addAddon((Addon) URLClassLoader.newInstance(new URL[] {f.toURI().toURL()}).loadClass(p.mainClass).newInstance(), p);
			} catch (Exception e) {
				Minecraft.logger.fatal("Failed to load addon " + f);
				e.printStackTrace();
			}
		
		final ModList mods = HackedClient.getClient().getMods();
		commands.add(new Command("#text", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.text.processCommand(t);}}));
		commands.add(new Command("#multitext", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.multiText.processCommand(t);}}));
		commands.add(new Command("#killpotion", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.killPotion.processCommand(t);}}));
		commands.add(new Command("#spam", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.spam.processCommand(t);}}));
		commands.add(new Command("#skinblink", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.skinBlinker.processCommand(t);}}));
		commands.add(new Command("#fastplace", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fastPlace.processCommand(t);}}));
		commands.add(new Command("#fastbreak", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fastBreak.processCommand(t);}}));
		commands.add(new Command("#nofall", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.nofall.processCommand(t);}}));
		commands.add(new Command("#fullbright", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fullbright.processCommand(t);}}));
		commands.add(new Command("#xray", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.xray.processCommand(t);}}));
		commands.add(new Command("#fasthit", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fasthit.processCommand(t);}}));
		commands.add(new Command("#autoclicker", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoclicker.processCommand(t);}}));
		commands.add(new Command("#noswing", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.noswing.processCommand(t);}}));
		commands.add(new Command("#authmecrack", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.authMeCrack.processCommand(t);}}));
		commands.add(new Command("#antiafk", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.antiAfk.processCommand(t);}}));
		commands.add(new Command("#autosteal", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autosteal.processCommand(t);}}));
		commands.add(new Command("#killaura", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.killaura.processCommand(t);}}));
		commands.add(new Command("#nuker", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.nuker.processCommand(t);}}));
		commands.add(new Command("#sneak", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.sneak.processCommand(t);}}));
		commands.add(new Command("#tracer", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.tracer.processCommand(t);}}));
		commands.add(new Command("#clearspam", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.spam.processCommand(t);}}));
		commands.add(new Command("#panic", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.panic.processCommand(t);}}));
		commands.add(new Command("#throw", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.thrower.processCommand(t);}}));
		commands.add(new Command("#flyvanilla", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.vanillaFly.processCommand(t);}}));
		commands.add(new Command("#masstpa", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.massTpa.processCommand(t);}}));
		commands.add(new Command("#autoarmor", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoArmor.processCommand(t);}}));
		commands.add(new Command("#trollpotion", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.trollPotion.processCommand(t);}}));
		commands.add(new Command("#twerk", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.twerk.processCommand(t);}}));
		commands.add(new Command("#fastladder", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fastLadder.processCommand(t);}}));
		commands.add(new Command("#reach", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.reach.processCommand(t);}}));
		commands.add(new Command("#velocity", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.velocity.processCommand(t);}}));
		commands.add(new Command("#flyac1", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.acFly1.processCommand(t);}}));
		commands.add(new Command("#flyac2", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.acFly2.processCommand(t);}}));
		commands.add(new Command("#timer", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.timer.processCommand(t);}}));
		commands.add(new Command("#speedac1", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.acSpeed1.processCommand(t);}}));
		commands.add(new Command("#sprint", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autosprint.processCommand(t);}}));
		commands.add(new Command("#bedfucker", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.bedFucker.processCommand(t);}}));
		commands.add(new Command("#speedlegit", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.legitSpeed.processCommand(t);}}));
		commands.add(new Command("#freecam", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.freecam.processCommand(t);}}));
		commands.add(new Command("#aimbot", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.aimbot.processCommand(t);}}));
		commands.add(new Command("#jailsmcbot", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.jailsmcBot.processCommand(t);}}));
		commands.add(new Command("#norender", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.noRender.processCommand(t);}}));
		commands.add(new Command("#stepjump", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.stepJump.processCommand(t);}}));
		commands.add(new Command("#jetpack", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.jetpack.processCommand(t);}}));
		commands.add(new Command("#regen", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.regen.processCommand(t);}}));
		commands.add(new Command("#flip", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.flip.processCommand(t);}}));
		commands.add(new Command("#lag", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.lag.processCommand(t);}}));
		commands.add(new Command("#scaffoldwalk", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.scaffoldWalk.processCommand(t);}}));
		commands.add(new Command("#fastfall", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fastFall.processCommand(t);}}));
		commands.add(new Command("#fasteat", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fastEat.processCommand(t);}}));
		commands.add(new Command("#autoswitch", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoSwitch.processCommand(t);}}));
		commands.add(new Command("#tired", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.tired.processCommand(t);}}));
		commands.add(new Command("#derp", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.derp.processCommand(t);}}));
		commands.add(new Command("#antipotion", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.antiPotion.processCommand(t);}}));
		commands.add(new Command("#nocobweb", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.noCobweb.processCommand(t);}}));
		commands.add(new Command("#parkour", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.parkour.processCommand(t);}}));
		commands.add(new Command("#phase", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.phase.processCommand(t);}}));
		commands.add(new Command("#fastbow", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.fastBow.processCommand(t);}}));
		commands.add(new Command("#spider", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.spider.processCommand(t);}}));
		commands.add(new Command("#antifire", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.antiFire.processCommand(t);}}));
		commands.add(new Command("#highjump", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.highJump.processCommand(t);}}));
		commands.add(new Command("#autowalk", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoWalk.processCommand(t);}}));
		commands.add(new Command("#autorespawn", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoRespawn.processCommand(t);}}));
		commands.add(new Command("#dolphin", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.dolphin.processCommand(t);}}));
		commands.add(new Command("#kaboom", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.kaboom.processCommand(t);}}));
		commands.add(new Command("#glide", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.glide.processCommand(t);}}));
		commands.add(new Command("#rollhead", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.rollHead.processCommand(t);}}));
		commands.add(new Command("#automine", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoMine.processCommand(t);}}));
		commands.add(new Command("#autosoup", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoSoup.processCommand(t);}}));
		commands.add(new Command("#autoleave", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoLeave.processCommand(t);}}));
		commands.add(new Command("#dropinventory", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.dropInventory.processCommand(t);}}));
		commands.add(new Command("#jesus", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.waterWalk.processCommand(t);}}));
		commands.add(new Command("#autojump", new Consumer<String[]>() {@Override public void accept(String[] t) {mods.autoJump.processCommand(t);}}));
		commands.add(new Command("#alt", new Consumer<String[]>() {@Override public void accept(String[] t) {String s = Util.combineParts(t, 1, " "); HackedClient.getClient().guiRenameWorld(s, new ChatGuiRenameWorld(s));}}));
		commands.add(new Command("#changelog", new Consumer<String[]>() {@Override public void accept(String[] t) {for(String s : Consts.changelog) Util.sendMessage(s);}}));
		commands.add(new Command("#credits", new Consumer<String[]>() {@Override public void accept(String[] t) {for(String s : Consts.credits) Util.sendMessage(s);}}));
	}

	/**
	 * Loads the addon's properties.
	 * @param f The file that should contain the addon
	 * @return The loaded props
	 * @throws ZipException
	 * @throws IOException
	 */
	AddonProperties loadProperties(File f) throws ZipException, IOException {
		String name = "";
		String author = "";
		String version = "";
		String desc = "";
		String main = "";
		String s;
		ZipFile zip = new ZipFile(f);
		BufferedReader br = new BufferedReader(
								new InputStreamReader(
									zip.getInputStream(zip.getEntry("c.addon")),
									StandardCharsets.UTF_8));
		
		while((s = br.readLine()) != null) {
			String[] tokens = s.split(" ");
				 if(tokens[0].equalsIgnoreCase("name"))    name    = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("author"))  author  = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("version")) version = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("desc"))    desc    = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("main"))    main    = tokens[1];
			else Util.info("Can't read addon config line \"" + s + "\".");
		}
		zip.close();
		return new AddonProperties(name, author, version, desc, main);
	}

	/**
	 * gets an addon by a name
	 * @param name the name of the addon
	 * @return the addon specified by the name
	 */
	public Addon getAddon(String name) {
		for(Entry<Addon, AddonProperties> e : addons.entrySet())
			if(e.getValue().name == name)
				return e.getKey();
		return null;
	}

	/**
	 * gets the props by the addon
	 * @param a the addon
	 * @return the properties of the addon
	 */
	public AddonProperties getProps(Addon a)
	{
		return addons.get(a);
	}

	/**
	 * adds the addon and the properties to the internal maps
	 * @param a the addon
	 * @param p the props of the addon
	 */
	public void addAddon(Addon a, AddonProperties p) {
		addons.put(a, p);
	}

	/**
	 * adds the commandexecutor to the list
	 * @param cmdExec the commandexecutor
	 */
	
	public void registerCommand(Command c)
	{
		commands.add(c);
	}

	/**
	 * executes the command
	 * @param args the ' '-splitted args
	 */
	public void execCmd(String[] args)
	{
		String cmd = args[0];
		for(Command c : commands)
			if(c.cmd.equalsIgnoreCase(cmd))
			{
				c.handler.accept(args);
				return;
			}
		Util.sendMessage(getHelp() + Consts.extraHelp);
	}

	public Set<Addon> getAddons()
	{
		return addons.keySet();
	}

	public String getName(Addon a)
	{
		return addons.get(a).name;
	}
	
	public int getBuildNumber()
	{
		return Consts.BLDNUM;
	}
	
	public String getHelp()
	{
		StringBuilder s = new StringBuilder("Commands:");
		for(Command c : commands)
		{
			s.append(' ');
			s.append(c.cmd);
		}
		return s.toString();
	}
}
