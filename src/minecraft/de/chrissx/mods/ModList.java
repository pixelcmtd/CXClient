package de.chrissx.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.chrissx.mods.building.AutoMine;
import de.chrissx.mods.building.BedFucker;
import de.chrissx.mods.building.FastBreak;
import de.chrissx.mods.building.FastPlace;
import de.chrissx.mods.building.Kaboom;
import de.chrissx.mods.building.Nuker;
import de.chrissx.mods.building.ScaffoldWalk;
import de.chrissx.mods.chat.AuthMeCrack;
import de.chrissx.mods.chat.Home;
import de.chrissx.mods.chat.MassTpa;
import de.chrissx.mods.chat.Spam;
import de.chrissx.mods.combat.Aimbot;
import de.chrissx.mods.combat.AntiFire;
import de.chrissx.mods.combat.AntiPotion;
import de.chrissx.mods.combat.AutoArmor;
import de.chrissx.mods.combat.AutoLeave;
import de.chrissx.mods.combat.AutoRespawn;
import de.chrissx.mods.combat.AutoSoup;
import de.chrissx.mods.combat.Autoclicker;
import de.chrissx.mods.combat.FastBow;
import de.chrissx.mods.combat.Fasthit;
import de.chrissx.mods.combat.Killaura;
import de.chrissx.mods.combat.Noswing;
import de.chrissx.mods.combat.Reach;
import de.chrissx.mods.fun.AutoSwitch;
import de.chrissx.mods.fun.Derp;
import de.chrissx.mods.fun.DropInventory;
import de.chrissx.mods.fun.JailsmcBot;
import de.chrissx.mods.fun.KillPotion;
import de.chrissx.mods.fun.MultiText;
import de.chrissx.mods.fun.RollHead;
import de.chrissx.mods.fun.SkinBlinker;
import de.chrissx.mods.fun.Text;
import de.chrissx.mods.fun.Throw;
import de.chrissx.mods.fun.Tired;
import de.chrissx.mods.fun.TrollPotion;
import de.chrissx.mods.fun.Twerk;
import de.chrissx.mods.movement.ACFly1;
import de.chrissx.mods.movement.ACFly2;
import de.chrissx.mods.movement.ACSpeed1;
import de.chrissx.mods.movement.AntiAfk;
import de.chrissx.mods.movement.AutoWalk;
import de.chrissx.mods.movement.Autosprint;
import de.chrissx.mods.movement.Dolphin;
import de.chrissx.mods.movement.FastFall;
import de.chrissx.mods.movement.FastLadder;
import de.chrissx.mods.movement.Flip;
import de.chrissx.mods.movement.Glide;
import de.chrissx.mods.movement.HighJump;
import de.chrissx.mods.movement.Jetpack;
import de.chrissx.mods.movement.LegitSpeed;
import de.chrissx.mods.movement.NoCobweb;
import de.chrissx.mods.movement.Nofall;
import de.chrissx.mods.movement.Parkour;
import de.chrissx.mods.movement.Phase;
import de.chrissx.mods.movement.Sneak;
import de.chrissx.mods.movement.Spider;
import de.chrissx.mods.movement.StepJump;
import de.chrissx.mods.movement.Timer;
import de.chrissx.mods.movement.VanillaFly;
import de.chrissx.mods.movement.Velocity;
import de.chrissx.mods.render.Freecam;
import de.chrissx.mods.render.Fullbright;
import de.chrissx.mods.render.NoRender;
import de.chrissx.mods.render.Tracer;
import de.chrissx.mods.render.Xray;

public class ModList implements Iterable<Mod> {

	public final SkinBlinker skinBlinker = new SkinBlinker();
	public final FastBreak fastBreak = new FastBreak();
	public final FastPlace fastPlace = new FastPlace();
	public final Spam spam = new Spam();
	public final Nofall nofall = new Nofall();
	public final Fullbright fullbright = new Fullbright();
	public final Xray xray = new Xray();
	public final Fasthit fasthit = new Fasthit();
	public final Autoclicker autoclicker = new Autoclicker();
	public final Noswing noswing = new Noswing();
	public final AuthMeCrack authMeCrack = new AuthMeCrack();
	public final AntiAfk antiAfk = new AntiAfk();
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

	public final Home home = new Home();
	public final Panic panic = new Panic();
	public final Text text = new Text();
	public final MultiText multiText = new MultiText();
	public final KillPotion killPotion = new KillPotion();
	public final TrollPotion trollPotion = new TrollPotion();
	public final Flip flip = new Flip();
	public final DropInventory dropInventory = new DropInventory();
	public final Throw thrower = new Throw();

	final Map<String, Bindable> bindable = new HashMap<String, Bindable>();

	public final int length;

	final Mod[] mods = new Mod[] {
			skinBlinker,
			fastBreak,
			fastPlace,
			spam,
			nofall,
			fullbright,
			xray,
			fasthit,
			autoclicker,
			noswing,
			authMeCrack,
			antiAfk,
			autosteal,
			killaura,
			nuker,
			sneak,
			tracer,
			massTpa,
			vanillaFly,
			autoArmor,
			twerk,
			fastLadder,
			reach,
			velocity,
			acSpeed1,
			stepJump,
			acFly1,
			acFly2,
			timer,
			legitSpeed,
			autosprint,
			bedFucker,
			freecam,
			aimbot,
			jailsmcBot,
			noRender,
			jetpack,
			regen,
			lag,
			scaffoldWalk,
			fastFall,
			fastEat,
			autoSwitch,
			tired,
			derp,
			antiPotion,
			noCobweb,
			parkour,
			phase,
			fastBow,
			spider,
			antiFire,
			highJump,
			autoWalk,
			autoRespawn,
			dolphin,
			kaboom,
			glide,
			rollHead,
			autoMine,
			autoSoup,
			autoLeave
	};
	public final List<RenderedObject> renderedObjects = new ArrayList<RenderedObject>();
	public final List<TickListener> tickListeners = new ArrayList<TickListener>();
	public final List<StopListener> stopListeners = new ArrayList<StopListener>();
	
	@SuppressWarnings("unlikely-arg-type")
	public ModList() {
		for(Mod m : mods)
			addBindable(m);
		
		addBindable(home);
		addBindable(panic);
		addBindable(trollPotion);
		addBindable(killPotion);
		addBindable(text);
		addBindable(multiText);
		addBindable(flip);
		addBindable(dropInventory);
		addBindable(thrower);
		
		for(Mod m : mods) {
			renderedObjects.add(m);
			tickListeners.add(m);
			stopListeners.add(m);
		}
		
		for(Bindable b : bindable.values()) {
			if(b instanceof RenderedObject && !renderedObjects.contains(b))
				renderedObjects.add((RenderedObject) b);
			if(b instanceof TickListener && !tickListeners.contains(b))
				tickListeners.add((TickListener) b);
			if(b instanceof StopListener && !stopListeners.contains(b))
				stopListeners.add((StopListener) b);
		}
		
		int len = 1;
		for(Mod m : mods)
		{
			len += m.getName().length();
			len += 2;
		}
		length = len;
	}
	
	public void addBindable(Bindable bindable) {
		this.bindable.put(bindable.getName().toLowerCase(), bindable);
	}
	
	public Set<Entry<String, Bindable>> getBindEntrys() {
		return bindable.entrySet();
	}
	
	public Bindable getBindable(String name) {
		return bindable.get(name.toLowerCase());
	}
	
	public Mod get(int i) {
		return mods[i];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator iterator() {
		return Arrays.asList(mods).iterator();
	}
}