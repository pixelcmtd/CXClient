package de.chrissx.mods;

import java.util.ArrayList;
import java.util.List;

import de.chrissx.mods.building.*;
import de.chrissx.mods.chat.*;
import de.chrissx.mods.combat.*;
import de.chrissx.mods.fun.*;
import de.chrissx.mods.movement.*;
import de.chrissx.mods.render.*;

public class ModList {

	public final IAUI iaui = new IAUI();
	public final SkinBlinker skinBlinker = new SkinBlinker();
	public final FastBreak fastBreak = new FastBreak();
	public final FastPlace fastPlace = new FastPlace();
	public final Spam spam = new Spam(false);
	public final Spam clearspam = new Spam(true);
	public final Nofall nofall = new Nofall();
	public final Fullbright fullbright = new Fullbright();
	public final Xray xray = new Xray();
	public final Fasthit fasthit = new Fasthit();
	public final Autoclicker autoclicker = new Autoclicker();
	public final Noswing noswing = new Noswing();
	public final AuthMeCrack authMeCrack = new AuthMeCrack();
	public final Afk afk = new Afk();
	public final Autosteal autosteal = new Autosteal();
	public final Killaura killaura = new Killaura();
	public final Nuker nuker = new Nuker();
	public final Sneak sneak = new Sneak();
	public final Tracer tracer = new Tracer();
	public final MassTpa massTpa = new MassTpa();
	public final VanillaFly vanillaFly = new VanillaFly();
	public final AutoArmor autoArmor = new AutoArmor();
	public final Twerk twerk = new Twerk();
	public final FastLadder fastLadder = new FastLadder();
	public final Reach reach = new Reach();
	public final Velocity velocity = new Velocity();
	public final ACFly1 acFly1 = new ACFly1();
	public final ACFly2 acFly2 = new ACFly2();
	public final Timer timer = new Timer();
	public final ACSpeed1 acSpeed1 = new ACSpeed1();
	public final StepJump stepJump = new StepJump();
	public final LegitSpeed legitSpeed = new LegitSpeed();
	public final Autosprint autosprint = new Autosprint();
	public final BedFucker bedFucker = new BedFucker();
	public final Freecam freecam = new Freecam();
	public final Aimbot aimbot = new Aimbot();
	public final JailsmcBot jailsmcBot = new JailsmcBot();
	public final NoRender noRender = new NoRender();
	public final Jetpack jetpack = new Jetpack();
	public final Regen regen = new Regen();
	public final Lag lag = new Lag();
	public final ScaffoldWalk scaffoldWalk = new ScaffoldWalk();
	public final FastFall fastFall = new FastFall();
	public final FastEat fastEat = new FastEat();
	public final AutoSwitch autoSwitch = new AutoSwitch();
	public final Tired tired = new Tired();
	public final Derp derp = new Derp();
	public final AntiPotion antiPotion = new AntiPotion();
	public final NoCobweb noCobweb = new NoCobweb();
	public final Parkour parkour = new Parkour();
	public final Phase phase = new Phase();
	public final FastBow fastBow = new FastBow();
	public final Spider spider = new Spider();
	public final AntiFire antiFire = new AntiFire();
	public final HighJump highJump = new HighJump();
	public final AutoWalk autoWalk = new AutoWalk();
	public final AutoRespawn autoRespawn = new AutoRespawn();
	public final Dolphin dolphin = new Dolphin();
	public final Kaboom kaboom = new Kaboom();
	public final Glide glide = new Glide();
	public final RollHead rollHead = new RollHead();
	public final AutoMine autoMine = new AutoMine();
	public final AutoSoup autoSoup = new AutoSoup();
	public final AutoLeave autoLeave = new AutoLeave();
	public final WaterWalk waterWalk = new WaterWalk();
	public final AutoJump autoJump = new AutoJump();
	public final InventoryWalk invWalk = new InventoryWalk();
	public final TriggerBot triggerBot = new TriggerBot();

	public final Home home = new Home();
	public final Panic panic = new Panic();
	public final Text text = new Text();
	public final MultiText multiText = new MultiText();
	public final KillPotion killPotion = new KillPotion();
	public final TrollPotion trollPotion = new TrollPotion();
	public final Flip flip = new Flip();
	public final DropInventory dropInventory = new DropInventory();
	public final Throw thrower = new Throw();

	public final Mod[] mods = new Mod[] { iaui, skinBlinker, fastBreak, fastPlace, spam, clearspam, nofall, fullbright, xray,
	                                      fasthit, autoclicker, noswing, authMeCrack, afk, autosteal, killaura, nuker, sneak, tracer, massTpa,
	                                      vanillaFly, autoArmor, twerk, fastLadder, reach, velocity, acSpeed1, stepJump, acFly1, acFly2, timer,
	                                      legitSpeed, autosprint, bedFucker, freecam, aimbot, jailsmcBot, noRender, jetpack, regen, lag, scaffoldWalk,
	                                      fastFall, fastEat, autoSwitch, tired, derp, antiPotion, noCobweb, parkour, phase, fastBow, spider, antiFire,
	                                      highJump, autoWalk, autoRespawn, dolphin, kaboom, glide, rollHead, autoMine, autoSoup, autoLeave, waterWalk,
	                                      autoJump, invWalk, triggerBot,
	                                    };
	public final List<Bindable> bindables = new ArrayList<Bindable>();
	public final List<RenderedObject> renderedObjects = new ArrayList<RenderedObject>();
	public final List<TickListener> tickListeners = new ArrayList<TickListener>();
	public final List<StopListener> stopListeners = new ArrayList<StopListener>();
	public final List<EapiModule> eapiModules = new ArrayList<EapiModule>();
	public final List<CommandExecutor> commandExecutors = new ArrayList<CommandExecutor>();
	public final List<ChatBot> chatBots = new ArrayList<ChatBot>();

	public ModList() {
		bindables.add(home);
		bindables.add(panic);
		bindables.add(trollPotion);
		bindables.add(killPotion);
		bindables.add(text);
		bindables.add(multiText);
		bindables.add(flip);
		bindables.add(dropInventory);
		bindables.add(thrower);

		for (Mod m : mods)
			bindables.add(m);

		bindables.sort((x, y) -> x.getName().toLowerCase().compareTo(y.getName().toLowerCase()));

		for (Bindable b : bindables) {
			if (b instanceof RenderedObject)
				renderedObjects.add((RenderedObject) b);
			if (b instanceof TickListener)
				tickListeners.add((TickListener) b);
			if (b instanceof StopListener)
				stopListeners.add((StopListener) b);
			if (b instanceof EapiModule)
				eapiModules.add((EapiModule) b);
			if (b instanceof CommandExecutor)
				commandExecutors.add((CommandExecutor) b);
			if (b instanceof ChatBot)
				chatBots.add((ChatBot) b);
		}
	}

	public Bindable getBindable(String name) {
		for(Bindable b : bindables)
			if(b.getName().equalsIgnoreCase(name))
				return b;
		return null;
	}
}
