package net.minecraft.client;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import de.chrissx.HackedClient;
import de.chrissx.util.Consts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Minecraft implements IThreadListener, IPlayerUsage
{
    public static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
    public static final boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;

    /** A 10MiB preallocation to ensure the heap is reasonably sized. */
    public static byte[] memoryReserve = new byte[10485760];
    private static final List<DisplayMode> macDisplayModes = Lists.newArrayList(new DisplayMode[] {new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)});
    private final File fileResourcepacks;
    private final PropertyMap twitchDetails;
    private final PropertyMap field_181038_N;
    private ServerData currentServerData;

    /** The RenderEngine instance used by Minecraft */
    private TextureManager renderEngine;

    /**
     * Set to 'this' in Minecraft constructor; used by some settings get methods
     */
    private static Minecraft theMinecraft;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private boolean enableGLErrorChecking = true;
    private boolean hasCrashed;

    /** Instance of CrashReport. */
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    private boolean field_181541_X = false;
    private Timer timer = new Timer(20); //ticks per seconds

    /** Instance of PlayerUsageSnooper. */
    private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
    public WorldClient theWorld;
    public RenderGlobal renderGlobal;
    private RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public EntityPlayerSP thePlayer;
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public EffectRenderer effectRenderer;
    public Session session;
    private boolean isGamePaused;

    /** The font renderer used for displaying and measuring text */
    public FontRenderer fontRendererObj;
    public FontRenderer standardGalacticFontRenderer;

    /** The GuiScreen that's being displayed at the moment. */
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;

    /** Mouse left click counter */
    public int leftClickCounter;

    /** Display width */
    private int tempDisplayWidth;

    /** Display height */
    private int tempDisplayHeight;

    /** Instance of IntegratedServer. */
    private IntegratedServer theIntegratedServer;

    /** Gui achievement */
    public GuiAchievement guiAchievement;
    public GuiIngame ingameGUI;

    /** Skip render world */
    public boolean skipRenderWorld;

    /** The ray trace hit that the mouse is over. */
    public MovingObjectPosition objectMouseOver;

    /** The game settings that currently hold effect. */
    public GameSettings gameSettings;

    /** Mouse helper instance. */
    public MouseHelper mouseHelper;
    public final File mcDataDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final Proxy proxy;
    private ISaveFormat saveLoader;

    /**
     * This is set to fpsCounter every debug screen update, and is shown on the debug screen. It's also sent as part of
     * the usage snooping.
     */
    private static int debugFPS;

    /**
     * When you place a block, it's set to 6, decremented once per tick, when it's 0, you can place another block.
     */
    public int rightClickDelayTimer;
    private String serverName;
    private int serverPort;

    /**
     * Does the actual gameplay have focus. If so then mouse and keys will effect the player instead of menus.
     */
    public boolean inGameHasFocus;
    long systemTime = getSystemTime();

    /** Join player counter */
    private int joinPlayerCounter;
    public final FrameTimer field_181542_y = new FrameTimer();
    long field_181543_z = System.nanoTime();
    private final boolean jvm64bit;
    private final boolean isDemo;
    private NetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;

    /** The profiler instance */
    public final Profiler mcProfiler = new Profiler();

    /**
     * Keeps track of how long the debug crash keycombo (F3+C) has been pressed for, in order to crash after 10 seconds.
     */
    private long debugCrashKeyPressTime = -1L;
    private IReloadableResourceManager mcResourceManager;
    private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
    private final List<IResourcePack> defaultResourcePacks = Lists.<IResourcePack>newArrayList();
    private final DefaultResourcePack mcDefaultResourcePack;
    private ResourcePackRepository mcResourcePackRepository;
    private LanguageManager mcLanguageManager;
    private IStream stream;
    private Framebuffer framebufferMc;
    private TextureMap textureMapBlocks;
    private SoundHandler mcSoundHandler;
    private MusicTicker mcMusicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue < FutureTask<? >> scheduledTasks = Queues. < FutureTask<? >> newArrayDeque();
    private long field_175615_aJ = 0L;
    private final Thread mcThread = Thread.currentThread();
    private ModelManager modelManager;

    /**
     * The BlockRenderDispatcher instance that will be used based off gamesettings
     */
    private BlockRendererDispatcher blockRenderDispatcher;

    /**
     * Set to true to keep the game loop running. Set to false by shutdown() to allow the game loop to exit cleanly.
     */
    volatile boolean running = true;

    /** String that shows the debug information */
    public String debug = "";
    public boolean field_175613_B = false;
    public boolean field_175614_C = false;
    public boolean field_175611_D = false;
    public boolean renderChunksMany = true;

    /** Approximate time (in ms) of last update to debug string */
    long debugUpdateTime = getSystemTime();

    /** holds the current fps */
    int fpsCounter;
    long prevFrameTime = -1L;

    /** Profiler currently displayed in the debug screen pie chart */
    String debugProfilerName = "root";
    
    HackedClient hc;

    public Minecraft(GameConfiguration gameConfig)
    {
        theMinecraft = this;
        this.mcDataDir = gameConfig.folderInfo.mcDataDir;
        this.fileAssets = gameConfig.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfig.gameInfo.version;
        this.twitchDetails = gameConfig.userInfo.userProperties;
        this.field_181038_N = gameConfig.userInfo.field_181172_c;
        this.mcDefaultResourcePack = new DefaultResourcePack((new ResourceIndex(gameConfig.folderInfo.assetsDir, gameConfig.folderInfo.assetIndex)).getResourceMap());
        this.proxy = gameConfig.userInfo.proxy == null ? Proxy.NO_PROXY : gameConfig.userInfo.proxy;
        this.sessionService = (new YggdrasilAuthenticationService(gameConfig.userInfo.proxy, UUID.randomUUID().toString())).createMinecraftSessionService();
        this.session = gameConfig.userInfo.session;
        logger.info("Setting user: " + this.session.getUsername());
        logger.info("(Session ID is " + this.session.getSessionID() + ")");
        this.isDemo = gameConfig.gameInfo.isDemo;
        this.displayWidth = gameConfig.displayInfo.width > 0 ? gameConfig.displayInfo.width : 1;
        this.displayHeight = gameConfig.displayInfo.height > 0 ? gameConfig.displayInfo.height : 1;
        this.tempDisplayWidth = gameConfig.displayInfo.width;
        this.tempDisplayHeight = gameConfig.displayInfo.height;
        this.fullscreen = gameConfig.displayInfo.fullscreen;
        this.jvm64bit = isJvm64bit();
        this.theIntegratedServer = new IntegratedServer(this);

        if (gameConfig.serverInfo.serverName != null)
        {
            this.serverName = gameConfig.serverInfo.serverName;
            this.serverPort = gameConfig.serverInfo.serverPort;
        }

        ImageIO.setUseCache(false);
        Bootstrap.register();
        
        hc = new HackedClient();
    }

    public void run()
    {
        this.running = true;

        try
        {
            startGame();
        }
        catch (Throwable t)
        {
            CrashReport cr = CrashReport.makeCrashReport(t, "Initializing game");
            cr.makeCategory("Initialization");
            displayCrashReport(addGraphicsAndWorldToCrashReport(cr));
            return;
        }

        while (true)
        {
            try
            {
                while (running)
                {
                    if (!hasCrashed || crashReporter == null)
                        try
                        {
                            runGameLoop();
                        }
                        catch (OutOfMemoryError oome)
                        {
                            displayGuiScreen(new GuiMemoryErrorScreen());
                        }
                    else
                        displayCrashReport(crashReporter);
                }
            }
            catch (MinecraftError me)
            {
                break;
            }
            catch (ReportedException re)
            {
                addGraphicsAndWorldToCrashReport(re.getCrashReport());
                logger.fatal((String)"Reported exception thrown!", (Throwable)re);
                displayCrashReport(re.getCrashReport());
                break;
            }
            catch (Throwable t)
            {
                CrashReport cr = addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", t));
                logger.fatal("Unreported exception thrown!", t);
                displayCrashReport(cr);
                break;
            }
            finally
            {
                shutdownMinecraftApplet();
            }

            return;
        }
    }

    /**
     * Starts the game: initializes the canvas, the title, the settings, etc.
     */
    void startGame() throws LWJGLException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        this.gameSettings = new GameSettings(this, mcDataDir);
        this.defaultResourcePacks.add(mcDefaultResourcePack);
        this.startTimerHackThread();

        if (gameSettings.overrideHeight > 0 && gameSettings.overrideWidth > 0)
        {
            displayWidth = gameSettings.overrideWidth;
            displayHeight = gameSettings.overrideHeight;
        }

        logger.info(Consts.clientName + " Version: " + Consts.version);
        logger.info("LWJGL Version: " + Sys.getVersion());
        setWindowIcon();
        setInitialDisplayMode();
        createDisplay();
        OpenGlHelper.initializeTextures();
        this.framebufferMc = new Framebuffer(displayWidth, displayHeight, true);
        this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.registerMetadataSerializers();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(mcDataDir, "server-resource-packs"), mcDefaultResourcePack, metadataSerializer_, gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(metadataSerializer_, gameSettings.language);
        this.mcResourceManager.registerReloadListener(mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(mcResourceManager);
        this.mcResourceManager.registerReloadListener(renderEngine);
        this.drawSplashScreen(renderEngine);
        this.initStream();
        this.skinManager = new SkinManager(renderEngine, new File(fileAssets, "skins"), sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(mcDataDir, "saves"));
        this.mcSoundHandler = new SoundHandler(mcResourceManager, gameSettings);
        this.mcResourceManager.registerReloadListener(mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(this);
        this.fontRendererObj = new FontRenderer(gameSettings, new ResourceLocation("textures/font/ascii.png"), renderEngine, false);

        if (this.gameSettings.language != null)
        {
            this.fontRendererObj.setUnicodeFlag(isUnicode());
            this.fontRendererObj.setBidiFlag(mcLanguageManager.isCurrentLanguageBidirectional());
        }

        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener(this.fontRendererObj);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat()
        {
            public String formatString(String fmt)
            {
                try
                {
                    return String.format(fmt, new Object[] {GameSettings.getKeyDisplayString(gameSettings.keyBindInventory.getKeyCode())});
                }
                catch (Exception exception)
                {
                    return "Error: " + exception.getLocalizedMessage();
                }
            }
        });
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0D);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.cullFace(1029);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        checkGLError("Startup");
        textureMapBlocks = new TextureMap("textures");
        textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
        renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
        renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        modelManager = new ModelManager(this.textureMapBlocks);
        mcResourceManager.registerReloadListener(this.modelManager);
        renderItem = new RenderItem(this.renderEngine, this.modelManager);
        renderManager = new RenderManager(this.renderEngine, this.renderItem);
        itemRenderer = new ItemRenderer(this);
        mcResourceManager.registerReloadListener(this.renderItem);
        entityRenderer = new EntityRenderer(this, this.mcResourceManager);
        mcResourceManager.registerReloadListener(this.entityRenderer);
        blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
        mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
        renderGlobal = new RenderGlobal(this);
        mcResourceManager.registerReloadListener(this.renderGlobal);
        guiAchievement = new GuiAchievement(this);
        GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
        effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        checkGLError("Post startup");
        ingameGUI = new GuiIngame(this);

        if (serverName != null)
            displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, serverName, serverPort));
        else
            displayGuiScreen(new GuiMainMenu());

        renderEngine.deleteTexture(mojangLogo);
        mojangLogo = null;
        loadingScreen = new LoadingScreenRenderer(this);

        if (gameSettings.fullScreen && !fullscreen)
            toggleFullscreen();

        try
        {
            Display.setVSyncEnabled(gameSettings.enableVsync);
        }
        catch (OpenGLException var2)
        {
            gameSettings.enableVsync = false;
            gameSettings.saveOptions();
        }

        renderGlobal.makeEntityOutlineShader();
    }

    void registerMetadataSerializers()
    {
        metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }

    void initStream()
    {
        try
        {
            stream = new TwitchStream(this, (Property)Iterables.getFirst(this.twitchDetails.get("twitch_access_token"), null));
        }
        catch (Throwable t)
        {
            stream = new NullStream(t);
            logger.error("Couldn\'t initialize twitch stream");
        }
    }

    void createDisplay() throws LWJGLException
    {
        Display.setResizable(true);
        Display.setTitle(Consts.clientName + " | Minecraft 1.8.8");

        try
        {
            Display.create((new PixelFormat()).withDepthBits(24));
        }
        catch (Throwable t)
        {
            logger.error("Couldn\'t set pixel format", t);

            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException var3){}

            if (fullscreen)
                updateDisplayMode();

            Display.create();
        }
    }

    void setInitialDisplayMode() throws LWJGLException
    {
        if (fullscreen)
        {
            Display.setFullscreen(true);
            DisplayMode displaymode = Display.getDisplayMode();
            displayWidth = Math.max(1, displaymode.getWidth());
            displayHeight = Math.max(1, displaymode.getHeight());
        }
        else
        {
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
        }
    }

    void setWindowIcon()
    {
        Util.EnumOS os = Util.getOSType();

        if (os != Util.EnumOS.OSX)
        {
            InputStream is1 = null;
            InputStream is2 = null;

            try
            {
                is1 = mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
                is2 = mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));

                if (is1 != null && is2 != null)
                {
                    Display.setIcon(new ByteBuffer[] {readImageToBuffer(is1), readImageToBuffer(is2)});
                }
            }
            catch (Throwable t)
            {
                logger.error("Couldn\'t set icon", t);
            }
            finally
            {
                IOUtils.closeQuietly(is1);
                IOUtils.closeQuietly(is2);
            }
        }
    }

    public static boolean isJvm64bit()
    {
        String[] astring = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

        for (String s : astring)
        {
            String s1 = System.getProperty(s);

            if (s1 != null && s1.contains("64"))
                return true;
        }

        return false;
    }

    public Framebuffer getFramebuffer()
    {
        return framebufferMc;
    }

    public String getVersion()
    {
        return launchedVersion;
    }

    private void startTimerHackThread()
    {
        Thread t = new Thread("Timer hack thread")
        {
            public void run()
            {
                while (running)
                {
                    try
                    {
                        Thread.sleep(2147483647);
                    }
                    catch (Throwable _t){}
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }

    public void crashed(CrashReport crash)
    {
        hasCrashed = true;
        crashReporter = crash;
    }

    /**
     * Wrapper around displayCrashReportInternal
     */
    public void displayCrashReport(CrashReport crashReportIn)
    {
        File file1 = new File(getMinecraft().mcDataDir, "crash-reports");
        File file2 = new File(file1, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());

        if (crashReportIn.getFile() != null)
        {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        }
        else if (crashReportIn.saveToFile(file2))
        {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
        }
        else
        {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }

    public boolean isUnicode()
    {
        return mcLanguageManager.isCurrentLocaleUnicode() || gameSettings.forceUnicodeFont;
    }

    public void refreshResources()
    {
        List<IResourcePack> list = Lists.newArrayList(defaultResourcePacks);

        for (ResourcePackRepository.Entry e : mcResourcePackRepository.getRepositoryEntries())
            list.add(e.getResourcePack());

        if (mcResourcePackRepository.getResourcePackInstance() != null)
            list.add(mcResourcePackRepository.getResourcePackInstance());

        try
        {
            mcResourceManager.reloadResources(list);
        }
        catch (Throwable t)
        {
            logger.info("Caught error stitching, removing all assigned resourcepacks", t);
            list.clear();
            list.addAll(defaultResourcePacks);
            mcResourcePackRepository.setRepositories(Collections.<ResourcePackRepository.Entry>emptyList());
            mcResourceManager.reloadResources(list);
            gameSettings.resourcePacks.clear();
            gameSettings.field_183018_l.clear();
            gameSettings.saveOptions();
        }

        mcLanguageManager.parseLanguageMetadata(list);

        if (renderGlobal != null)
            renderGlobal.loadRenderers();
    }

    ByteBuffer readImageToBuffer(InputStream s) throws IOException
    {
        BufferedImage i = ImageIO.read(s);
        int[] j = i.getRGB(0, 0, i.getWidth(), i.getHeight(), null, 0, i.getWidth());
        ByteBuffer b = ByteBuffer.allocate(4 * j.length);

        for (int k : j)
            b.putInt(k << 8 | k >> 24 & 255);

        b.flip();
        return b;
    }

    void updateDisplayMode() throws LWJGLException
    {
        Set<DisplayMode> set = Sets.<DisplayMode>newHashSet();
        Collections.addAll(set, Display.getAvailableDisplayModes());
        DisplayMode dm = Display.getDesktopDisplayMode();

        if (!set.contains(dm) && Util.getOSType() == Util.EnumOS.OSX)
        {
            label53:

            for (DisplayMode dm1 : macDisplayModes)
            {
                boolean flag = true;

                for (DisplayMode dm2 : set)
                {
                    if (dm2.getBitsPerPixel() == 32 && dm2.getWidth() == dm1.getWidth() && dm2.getHeight() == dm1.getHeight())
                    {
                        flag = false;
                        break;
                    }
                }

                if (!flag)
                {
                    Iterator i = set.iterator();
                    DisplayMode dm3;

                    while (true)
                    {
                        if (!i.hasNext())
                            continue label53;

                        dm3 = (DisplayMode)i.next();

                        if (dm3.getBitsPerPixel() == 32 && dm3.getWidth() == dm1.getWidth() / 2 && dm3.getHeight() == dm1.getHeight() / 2)
                            break;
                    }

                    dm = dm3;
                }
            }
        }

        Display.setDisplayMode(dm);
        displayWidth = dm.getWidth();
        displayHeight = dm.getHeight();
    }

    private void drawSplashScreen(TextureManager tm) throws LWJGLException
    {
        ScaledResolution sr = new ScaledResolution(this);
        int i = sr.getScaleFactor();
        Framebuffer fb = new Framebuffer(sr.getScaledWidth() * i, sr.getScaledHeight() * i, true);
        fb.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, sr.getScaledWidth(), sr.getScaledHeight(), 0, 1000, 3000);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, 0, -2000);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        InputStream is = null;

        try
        {
            is = mcDefaultResourcePack.getInputStream(locationMojangPng);
            mojangLogo = tm.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(is)));
            tm.bindTexture(mojangLogo);
        }
        catch (IOException e)
        {
            logger.error("Unable to load logo: " + locationMojangPng, (Throwable)e);
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }

        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos(0.0D, (double)this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        wr.pos((double)this.displayWidth, (double)this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        wr.pos((double)this.displayWidth, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        wr.pos(0, 0, 0).tex(0, 0).color(255, 255, 255, 255).endVertex();
        t.draw();
        GlStateManager.color(1, 1, 1, 1);
        int j = 256;
        int k = 256;
        this.func_181536_a((sr.getScaledWidth() - j) / 2, (sr.getScaledHeight() - k) / 2, 0, 0, j, k, 255, 255, 255, 255);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        fb.unbindFramebuffer();
        fb.framebufferRender(sr.getScaledWidth() * i, sr.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        this.updateDisplay();
    }

    public void func_181536_a(int i, int j, int k, int l, int m, int n, int o, int p, int q, int r)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos((double)i, (double)(j + n), 0.0D).tex((double)((float)k * f), (double)((float)(l + n) * f1)).color(o, p, q, r).endVertex();
        wr.pos((double)(i + m), (double)(j + n), 0.0D).tex((double)((float)(k + m) * f), (double)((float)(l + n) * f1)).color(o, p, q, r).endVertex();
        wr.pos((double)(i + m), (double)j, 0.0D).tex((double)((float)(k + m) * f), (double)((float)l * f1)).color(o, p, q, r).endVertex();
        wr.pos((double)i, (double)j, 0.0D).tex((double)((float)k * f), (double)((float)l * f1)).color(o, p, q, r).endVertex();
        Tessellator.getInstance().draw();
    }

    /**
     * Returns the save loader that is currently being used
     */
    public ISaveFormat getSaveLoader()
    {
        return this.saveLoader;
    }

    /**
     * Sets the argument GuiScreen as the main (topmost visible) screen.
     */
    public void displayGuiScreen(GuiScreen guiScreenIn)
    {
        if (currentScreen != null)
            currentScreen.onGuiClosed();

        if (guiScreenIn == null && theWorld == null)
            guiScreenIn = new GuiMainMenu();
        else if (guiScreenIn == null && thePlayer.getHealth() <= 0)
            guiScreenIn = new GuiGameOver();

        if (guiScreenIn instanceof GuiMainMenu)
        {
            gameSettings.showDebugInfo = false;
            ingameGUI.getChatGUI().clearChatMessages();
        }

        currentScreen = (GuiScreen)guiScreenIn;

        if (guiScreenIn != null)
        {
            setIngameNotInFocus();
            ScaledResolution sr = new ScaledResolution(this);
            int i = sr.getScaledWidth();
            int j = sr.getScaledHeight();
            ((GuiScreen)guiScreenIn).setWorldAndResolution(this, i, j);
            skipRenderWorld = false;
        }
        else
        {
            mcSoundHandler.resumeSounds();
            setIngameFocus();
        }
    }

    /**
     * Checks for an OpenGL error. If there is one, prints the error ID and error string.
     */
    private void checkGLError(String s)
    {
        if (enableGLErrorChecking)
        {
            int i = GL11.glGetError();

            if (i != 0)
            {
                String t = GLU.gluErrorString(i);
                logger.error("########## GL ERROR ##########");
                logger.error("@ " + s);
                logger.error(i + ": " + t);
            }
        }
    }

    /**
     * Shuts down the minecraft applet by stopping the resource downloads, and clearing up GL stuff; called when the
     * application (or web page) is exited.
     */
    public void shutdownMinecraftApplet()
    {
        try
        {
            stream.shutdownStream();
            logger.info("Stopping " + Consts.clientName + ".");

            try
            {
                loadWorld(null);
            }
            catch (Throwable t){}

            mcSoundHandler.unloadSounds();
        }
        finally
        {
            Display.destroy();

            if (!hasCrashed)
                System.exit(0);
        }

    }

    /**
     * Called repeatedly from run()
     */
    void runGameLoop() throws IOException
    {
        long i = System.nanoTime();
        mcProfiler.startSection("root");

        if (Display.isCreated() && Display.isCloseRequested())
            shutdown();

        if (isGamePaused && theWorld != null)
        {
            float f = timer.renderPartialTicks;
            timer.updateTimer();
            timer.renderPartialTicks = f;
        }
        else
            timer.updateTimer();

        mcProfiler.startSection("scheduledExecutables");

        synchronized (scheduledTasks)
        {
            while (!scheduledTasks.isEmpty())
            {
                Util.runFutureTask((FutureTask)scheduledTasks.poll(), logger);
            }
        }

        mcProfiler.endSection();
        long l = System.nanoTime();
        mcProfiler.startSection("tick");

        for (int j = 0; j < timer.elapsedTicks; ++j)
            runTick();

        mcProfiler.endStartSection("preRenderErrors");
        long i1 = System.nanoTime() - l;
        checkGLError("Pre render");
        mcProfiler.endStartSection("sound");
        mcSoundHandler.setListener(this.thePlayer, this.timer.renderPartialTicks);
        mcProfiler.endSection();
        mcProfiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        framebufferMc.bindFramebuffer(true);
        mcProfiler.startSection("display");
        GlStateManager.enableTexture2D();

        if (thePlayer != null && thePlayer.isEntityInsideOpaqueBlock())
            gameSettings.thirdPersonView = 0;

        mcProfiler.endSection();

        if (!skipRenderWorld)
        {
            mcProfiler.endStartSection("gameRenderer");
            entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
            mcProfiler.endSection();
        }

        mcProfiler.endSection();

        if (gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart && !gameSettings.hideGUI)
        {
            if (!mcProfiler.profilingEnabled)
                mcProfiler.clearProfiling();

            mcProfiler.profilingEnabled = true;
            displayDebugInfo(i1);
        }
        else
        {
            mcProfiler.profilingEnabled = false;
            prevFrameTime = System.nanoTime();
        }

        guiAchievement.updateAchievementWindow();
        framebufferMc.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        entityRenderer.renderStreamIndicator(this.timer.renderPartialTicks);
        GlStateManager.popMatrix();
        mcProfiler.startSection("root");
        updateDisplay();
        Thread.yield();
        mcProfiler.startSection("stream");
        mcProfiler.startSection("update");
        stream.func_152935_j();
        mcProfiler.endStartSection("submit");
        stream.func_152922_k();
        mcProfiler.endSection();
        mcProfiler.endSection();
        checkGLError("Post render");
        ++fpsCounter;
        isGamePaused = isSingleplayer() && currentScreen != null && currentScreen.doesGuiPauseGame() && !theIntegratedServer.getPublic();
        long k = System.nanoTime();
        field_181542_y.func_181747_a(k - field_181543_z);
        field_181543_z = k;

        while (getSystemTime() >= debugUpdateTime + 1000)
        {
            debugFPS = fpsCounter;
            debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", new Object[]
            		{debugFPS, RenderChunk.renderChunksUpdated, RenderChunk.renderChunksUpdated != 1 ? "s" : "",
            				(float)gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : gameSettings.limitFramerate, gameSettings.enableVsync ? " vsync" : "",
            						gameSettings.fancyGraphics ? "" : " fast", gameSettings.clouds == 0 ? "" : (gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : ""});
            RenderChunk.renderChunksUpdated = 0;
            debugUpdateTime += 1000L;
            fpsCounter = 0;
            usageSnooper.addMemoryStatsToSnooper();

            if (!usageSnooper.isSnooperRunning())
                usageSnooper.startSnooper();
        }

        if (isFramerateLimitBelowMax())
        {
            mcProfiler.startSection("fpslimit_wait");
            Display.sync(gameSettings.limitFramerate);
            mcProfiler.endSection();
        }

        mcProfiler.endSection();
    }

    public void updateDisplay()
    {
        mcProfiler.startSection("display_update");
        Display.update();
        mcProfiler.endSection();
        checkWindowResize();
    }

    protected void checkWindowResize()
    {
        if (!this.fullscreen && Display.wasResized() && (displayWidth != Display.getWidth() || displayHeight != Display.getHeight()))
            resize((displayWidth = Display.getWidth() <= 0 ? 1 : Display.getWidth()),
            		(displayHeight = Display.getHeight() <= 0 ? 1 : Display.getHeight()));
    }

    public boolean isFramerateLimitBelowMax()
    {
        return (float)gameSettings.limitFramerate < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }

    /**
     * Update debugProfilerName in response to number keys in debug screen
     */
    private void updateDebugProfilerName(int keyCount)
    {
        List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);

        if (list != null && !list.isEmpty())
        {
            Profiler.Result profiler$result = (Profiler.Result)list.remove(0);

            if (keyCount == 0)
            {
                if (profiler$result.field_76331_c.length() > 0)
                {
                    int i = this.debugProfilerName.lastIndexOf(".");

                    if (i >= 0)
                        this.debugProfilerName = this.debugProfilerName.substring(0, i);
                }
            }
            else
            {
                --keyCount;

                if (keyCount < list.size() && !((Profiler.Result)list.get(keyCount)).field_76331_c.equals("unspecified"))
                {
                    if (this.debugProfilerName.length() > 0)
                        this.debugProfilerName = this.debugProfilerName + ".";

                    this.debugProfilerName = this.debugProfilerName + ((Profiler.Result)list.get(keyCount)).field_76331_c;
                }
            }
        }
    }

    /**
     * Parameter appears to be unused.
     */
    private void displayDebugInfo(long elapsedTicksTime)
    {
        if (this.mcProfiler.profilingEnabled)
        {
            List<Profiler.Result> list = mcProfiler.getProfilingData(debugProfilerName);
            Profiler.Result pr = (Profiler.Result)list.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0, (double)displayWidth, (double)displayHeight, 0, 1000, 3000);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0, 0, -2000);
            GL11.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 160;
            int j = displayWidth - i - 10;
            int k = displayHeight - i * 2;
            GlStateManager.enableBlend();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double)((float)j - (float)i * 1.1F), (double)((float)k - (float)i * 0.6F - 16.0F), 0.0D).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos((double)((float)j - (float)i * 1.1F), (double)(k + i * 2), 0.0D).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos((double)((float)j + (float)i * 1.1F), (double)(k + i * 2), 0.0D).color(200, 0, 0, 0).endVertex();
            worldrenderer.pos((double)((float)j + (float)i * 1.1F), (double)((float)k - (float)i * 0.6F - 16.0F), 0.0D).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d = 0;

            for (int l = 0; l < list.size(); ++l)
            {
                Profiler.Result pr1 = (Profiler.Result)list.get(l);
                int i1 = MathHelper.floor(pr1.field_76332_a / 4.0D) + 1;
                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                int j1 = pr1.func_76329_a();
                int k1 = j1 >> 16 & 255;
                int l1 = j1 >> 8 & 255;
                int i2 = j1 & 255;
                worldrenderer.pos((double)j, (double)k, 0).color(k1, l1, i2, 255).endVertex();

                for (int j2 = i1; j2 >= 0; --j2)
                {
                    float f = (float)((d + pr1.field_76332_a * (double)j2 / (double)i1) * Math.PI * 2 / 100);
                    float f1 = MathHelper.sin(f) * (float)i;
                    float f2 = MathHelper.cos(f) * (float)i * 0.5F;
                    worldrenderer.pos((double)((float)j + f1), (double)((float)k - f2), 0).color(k1, l1, i2, 255).endVertex();
                }

                tessellator.draw();
                worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);

                for (int i3 = i1; i3 >= 0; --i3)
                {
                    float f3 = (float)((d + pr1.field_76332_a * (double)i3 / (double)i1) * Math.PI * 2.0D / 100.0D);
                    float f4 = MathHelper.sin(f3) * (float)i;
                    float f5 = MathHelper.cos(f3) * (float)i * 0.5F;
                    worldrenderer.pos((double)((float)j + f4), (double)((float)k - f5), 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                    worldrenderer.pos((double)((float)j + f4), (double)((float)k - f5 + 10.0F), 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                }

                tessellator.draw();
                d += pr1.field_76332_a;
            }

            DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String s = "";

            if (!pr.field_76331_c.equals("unspecified"))
                s = s + "[0] ";

            if (pr.field_76331_c.length() == 0)
                s = s + "ROOT ";
            else
                s = s + pr.field_76331_c + " ";

            int l2 = 16777215;
            this.fontRendererObj.drawStringWithShadow(s, (float)(j - i), (float)(k - i / 2 - 16), l2);
            this.fontRendererObj.drawStringWithShadow(s = decimalformat.format(pr.field_76330_b) + "%", (float)(j + i - this.fontRendererObj.getStringWidth(s)), (float)(k - i / 2 - 16), l2);

            for (int k2 = 0; k2 < list.size(); ++k2)
            {
                Profiler.Result pr2 = (Profiler.Result)list.get(k2);
                String s1 = "";

                if (pr2.field_76331_c.equals("unspecified"))
                    s1 = s1 + "[?] ";
                else
                    s1 = s1 + "[" + (k2 + 1) + "] ";

                s1 = s1 + pr2.field_76331_c;
                fontRendererObj.drawStringWithShadow(s1, (float)(j - i), (float)(k + i / 2 + k2 * 8 + 20), pr2.func_76329_a());
                fontRendererObj.drawStringWithShadow(s1 = decimalformat.format(pr2.field_76332_a) + "%", (float)(j + i - 50 - fontRendererObj.getStringWidth(s1)), (float)(k + i / 2 + k2 * 8 + 20), pr2.func_76329_a());
                fontRendererObj.drawStringWithShadow(s1 = decimalformat.format(pr2.field_76330_b) + "%", (float)(j + i - fontRendererObj.getStringWidth(s1)), (float)(k + i / 2 + k2 * 8 + 20), pr2.func_76329_a());
            }
        }
    }

    /**
     * Called when the window is closing. Sets 'running' to false which allows the game loop to exit cleanly.
     */
    public void shutdown()
    {
		hc.onShutdown();
        running = false;
    }

    /**
     * Will set the focus to ingame if the Minecraft window is the active with focus. Also clears any GUI screen
     * currently displayed
     */
    public void setIngameFocus()
    {
        if (Display.isActive())
        {
            if (!inGameHasFocus)
            {
                inGameHasFocus = true;
                mouseHelper.grabMouseCursor();
                displayGuiScreen(null);
                leftClickCounter = 10000;
            }
        }
    }

    /**
     * Resets the player keystate, disables the ingame focus, and ungrabs the mouse cursor.
     */
    public void setIngameNotInFocus()
    {
        if (this.inGameHasFocus)
        {
            KeyBinding.unPressAllKeys();
            inGameHasFocus = false;
            mouseHelper.ungrabMouseCursor();
        }
    }

    /**
     * Displays the ingame menu.
     */
    public void displayInGameMenu()
    {
        if (currentScreen == null)
        {
            displayGuiScreen(new GuiIngameMenu());

            if (isSingleplayer() && !theIntegratedServer.getPublic())
                mcSoundHandler.pauseSounds();
        }
    }

    private void sendClickBlockToController(boolean leftClick)
    {
        if (!leftClick)
            leftClickCounter = 0;

        if (leftClickCounter <= 0 && !thePlayer.isUsingItem())
        {
            if (leftClick && objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos blockpos = objectMouseOver.getBlockPos();

                if (theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air && playerController.onPlayerDamageBlock(blockpos, objectMouseOver.sideHit))
                {
                    effectRenderer.addBlockHitEffects(blockpos, objectMouseOver.sideHit);
                    if(!hc.getMods().noswing.isEnabled())
                    	thePlayer.swingItem();
                }
            }
            else
                playerController.resetBlockRemoving();
        }
    }

    public void clickMouse()
    {
        if (this.leftClickCounter <= 0)
        {
        	if(!hc.getMods().noswing.isEnabled())
        		thePlayer.swingItem();

            if (objectMouseOver == null)
            {
                logger.error("Null returned as \'hitResult\', this shouldn\'t happen!");

                if (playerController.isNotCreative())
                    leftClickCounter = 10;
            }
            else
            {
                switch (this.objectMouseOver.typeOfHit)
                {
                    case ENTITY:
                        playerController.attackEntity(thePlayer, objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = objectMouseOver.getBlockPos();

                        if (theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
                        {
                            playerController.clickBlock(blockpos, objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                    default:
                        if (playerController.isNotCreative())
                            leftClickCounter = 10;
                }
            }
        }
    }

    @SuppressWarnings("incomplete-switch")
	public void rightClickMouse()
    {
        if (!playerController.func_181040_m())
        {
            rightClickDelayTimer = 4;
            boolean flag = true;
            ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();

            if (this.objectMouseOver == null)
                logger.warn("Null returned as \'hitResult\', this shouldn\'t happen!");
            else
            {
                switch (this.objectMouseOver.typeOfHit)
                {
                    case ENTITY:
                        if (this.playerController.func_178894_a(this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver))
                        {
                            flag = false;
                        }
                        else if (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit))
                        {
                            flag = false;
                        }

                        break;

                    case BLOCK:
                        BlockPos blockpos = this.objectMouseOver.getBlockPos();

                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
                        {
                            int i = itemstack != null ? itemstack.stackSize : 0;

                            if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemstack, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec))
                            {
                                flag = false;
                                if(!hc.getMods().noswing.isEnabled())
                                	this.thePlayer.swingItem();
                            }

                            if (itemstack == null)
                            {
                                return;
                            }

                            if (itemstack.stackSize == 0)
                            {
                                this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                            }
                            else if (itemstack.stackSize != i || this.playerController.isInCreativeMode())
                            {
                                this.entityRenderer.itemRenderer.resetEquippedProgress();
                            }
                        }
                }
            }

            if (flag)
            {
                ItemStack is = this.thePlayer.inventory.getCurrentItem();
                if (is != null && playerController.sendUseItem(thePlayer, theWorld, is))
                    entityRenderer.itemRenderer.resetEquippedProgress2();
            }
        }
    }

    /**
     * Toggles fullscreen mode.
     */
    public void toggleFullscreen()
    {
        try
        {
            fullscreen = !fullscreen;
            gameSettings.fullScreen = fullscreen;

            if (fullscreen)
            {
                updateDisplayMode();
                displayWidth = Display.getDisplayMode().getWidth();
                displayHeight = Display.getDisplayMode().getHeight();

                if (displayWidth <= 0)
                    displayWidth = 1;

                if (displayHeight <= 0)
                    displayHeight = 1;
            }
            else
            {
                Display.setDisplayMode(new DisplayMode(tempDisplayWidth, tempDisplayHeight));
                displayWidth = tempDisplayWidth;
                displayHeight = tempDisplayHeight;

                if (displayWidth <= 0)
                    displayWidth = 1;

                if (displayHeight <= 0)
                    displayHeight = 1;
            }

            if (currentScreen != null)
                resize(displayWidth, displayHeight);
            else
                updateFramebufferSize();

            Display.setFullscreen(fullscreen);
            Display.setVSyncEnabled(gameSettings.enableVsync);
            updateDisplay();
        }
        catch (Throwable t)
        {
            logger.error("Couldn\'t toggle fullscreen", t);
        }
    }

    /**
     * Called to resize the current screen.
     */
    void resize(int width, int height)
    {
        displayWidth = Math.max(1, width);
        displayHeight = Math.max(1, height);

        if (this.currentScreen != null)
        {
            ScaledResolution sr = new ScaledResolution(this);
            currentScreen.onResize(this, sr.getScaledWidth(), sr.getScaledHeight());
        }

        loadingScreen = new LoadingScreenRenderer(this);
        updateFramebufferSize();
    }

    void updateFramebufferSize()
    {
        framebufferMc.createBindFramebuffer(displayWidth, displayHeight);

        if (entityRenderer != null)
            entityRenderer.updateShaderGroupSize(displayWidth, displayHeight);
    }

    public MusicTicker func_181535_r()
    {
        return mcMusicTicker;
    }

    /**
     * Runs the current tick.
     */
    public void runTick() throws IOException
    {
    	hc.onTick();

        if (rightClickDelayTimer > 0)
            --rightClickDelayTimer;

        this.mcProfiler.startSection("gui");

        if (!isGamePaused)
            ingameGUI.updateTick();

        mcProfiler.endSection();
        entityRenderer.getMouseOver(1);
        mcProfiler.startSection("gameMode");

        if (!isGamePaused && theWorld != null)
            playerController.updateController();

        mcProfiler.endStartSection("textures");

        if (!isGamePaused)
            renderEngine.tick();

        if (currentScreen == null && thePlayer != null)
        {
            if (thePlayer.getHealth() <= 0)
                displayGuiScreen(null);
            else if (thePlayer.isPlayerSleeping() && theWorld != null)
                displayGuiScreen(new GuiSleepMP());
        }
        else if (currentScreen != null && currentScreen instanceof GuiSleepMP && !thePlayer.isPlayerSleeping())
            displayGuiScreen(null);

        if (currentScreen != null)
            leftClickCounter = 10000;

        if (currentScreen != null)
        {
            try
            {
                currentScreen.handleInput();
            }
            catch (Throwable t)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(t, "Updating screen events");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>()
                {
                    public String call() throws Exception
                    {
                        return currentScreen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }

            if (currentScreen != null)
                try
                {
                    currentScreen.updateScreen();
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Ticking screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
                    crashreportcategory1.addCrashSectionCallable("Screen name", new Callable<String>()
                    {
                        public String call() throws Exception
                        {
                            return currentScreen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(crashreport1);
                }
        }

        if (this.currentScreen == null || this.currentScreen.allowUserInput)
        {
            this.mcProfiler.endStartSection("mouse");

            while (Mouse.next())
            {
                int i = Mouse.getEventButton();
                KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());

                if (Mouse.getEventButtonState())
                    if (this.thePlayer.isSpectator() && i == 2)
                        this.ingameGUI.getSpectatorGui().func_175261_b();
                    else
                        KeyBinding.onTick(i - 100);

                long i1 = getSystemTime() - systemTime;

                if (i1 <= 200L)
                {
                    int j = Mouse.getEventDWheel();

                    if (j != 0)
                    {
                        if (this.thePlayer.isSpectator())
                        {
                            j = j < 0 ? -1 : 1;

                            if (this.ingameGUI.getSpectatorGui().func_175262_a())
                                this.ingameGUI.getSpectatorGui().func_175259_b(-j);
                            else
                            {
                                float f = MathHelper.clamp(this.thePlayer.capabilities.getFlySpeed() + (float)j * 0.005F, 0.0F, 0.2F);
                                this.thePlayer.capabilities.setFlySpeed(f);
                            }
                        }
                        else
                            this.thePlayer.inventory.changeCurrentItem(j);
                    }

                    if (this.currentScreen == null)
                    {
                        if (!this.inGameHasFocus && Mouse.getEventButtonState())
                            this.setIngameFocus();
                    }
                    else if (this.currentScreen != null)
                        this.currentScreen.handleMouseInput();
                }
            }

            if (this.leftClickCounter > 0)
            {
                --this.leftClickCounter;
            }

            this.mcProfiler.endStartSection("keyboard");

            while (Keyboard.next())
            {
                int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
                KeyBinding.setKeyBindState(k, Keyboard.getEventKeyState());

                if (Keyboard.getEventKeyState())
                {
                    KeyBinding.onTick(k);
                }

                if (this.debugCrashKeyPressTime > 0L)
                {
                    if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L)
                    {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }

                    if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61))
                    {
                        this.debugCrashKeyPressTime = -1L;
                    }
                }
                else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61))
                {
                    this.debugCrashKeyPressTime = getSystemTime();
                }

                this.dispatchKeypresses();

                if (Keyboard.getEventKeyState())
                {
                    if (k == 62 && this.entityRenderer != null)
                    {
                        this.entityRenderer.switchUseShader();
                    }

                    if (this.currentScreen != null)
                    {
                        this.currentScreen.handleKeyboardInput();
                    }
                    else
                    {
                        if (k == 1)
                        {
                            this.displayInGameMenu();
                        }

                        if (k == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null)
                        {
                            this.ingameGUI.getChatGUI().clearChatMessages();
                        }

                        if (k == 31 && Keyboard.isKeyDown(61))
                        {
                            this.refreshResources();
                        }

                        if (k == 17 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (k == 18 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (k == 47 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (k == 38 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (k == 22 && Keyboard.isKeyDown(61))
                        {
                            ;
                        }

                        if (k == 20 && Keyboard.isKeyDown(61))
                        {
                            this.refreshResources();
                        }

                        if (k == 33 && Keyboard.isKeyDown(61))
                        {
                            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
                        }

                        if (k == 30 && Keyboard.isKeyDown(61))
                        {
                            this.renderGlobal.loadRenderers();
                        }

                        if (k == 35 && Keyboard.isKeyDown(61))
                        {
                            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                            this.gameSettings.saveOptions();
                        }

                        if (k == 48 && Keyboard.isKeyDown(61))
                        {
                            this.renderManager.setDebugBoundingBox(!this.renderManager.isDebugBoundingBox());
                        }

                        if (k == 25 && Keyboard.isKeyDown(61))
                        {
                            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                            this.gameSettings.saveOptions();
                        }

                        if (k == 59)
                        {
                            this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                        }

                        if (k == 61)
                        {
                            this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                            this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                            this.gameSettings.field_181657_aC = GuiScreen.isAltKeyDown();
                        }

                        if (this.gameSettings.keyBindTogglePerspective.isPressed())
                        {
                            ++this.gameSettings.thirdPersonView;

                            if (this.gameSettings.thirdPersonView > 2)
                            {
                                this.gameSettings.thirdPersonView = 0;
                            }

                            if (this.gameSettings.thirdPersonView == 0)
                            {
                                this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
                            }
                            else if (this.gameSettings.thirdPersonView == 1)
                            {
                                this.entityRenderer.loadEntityShader((Entity)null);
                            }

                            this.renderGlobal.setDisplayListEntitiesDirty();
                        }

                        if (this.gameSettings.keyBindSmoothCamera.isPressed())
                        {
                            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                        }
                    }

                    if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart)
                    {
                        if (k == 11)
                        {
                            this.updateDebugProfilerName(0);
                        }

                        for (int j1 = 0; j1 < 9; ++j1)
                        {
                            if (k == 2 + j1)
                            {
                                this.updateDebugProfilerName(j1 + 1);
                            }
                        }
                    }
                }
            }

            for (int l = 0; l < 9; ++l)
            {
                if (this.gameSettings.keyBindsHotbar[l].isPressed())
                {
                    if (this.thePlayer.isSpectator())
                    {
                        this.ingameGUI.getSpectatorGui().func_175260_a(l);
                    }
                    else
                    {
                        this.thePlayer.inventory.currentItem = l;
                    }
                }
            }

            boolean flag = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

            while (this.gameSettings.keyBindInventory.isPressed())
            {
                if (this.playerController.isRidingHorse())
                {
                    this.thePlayer.sendHorseInventory();
                }
                else
                {
                    this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    this.displayGuiScreen(new GuiInventory(this.thePlayer));
                }
            }

            while (this.gameSettings.keyBindDrop.isPressed())
            {
                if (!this.thePlayer.isSpectator())
                {
                    this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                }
            }

            while (this.gameSettings.keyBindChat.isPressed() && flag)
            {
                this.displayGuiScreen(new GuiChat());
            }

            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && flag)
            {
                this.displayGuiScreen(new GuiChat("/"));
            }

            if (this.thePlayer.isUsingItem())
            {
                if (!this.gameSettings.keyBindUseItem.isKeyDown())
                {
                    this.playerController.onStoppedUsingItem(this.thePlayer);
                }

                while (this.gameSettings.keyBindAttack.isPressed())
                {
                    ;
                }

                while (this.gameSettings.keyBindUseItem.isPressed())
                {
                    ;
                }

                while (this.gameSettings.keyBindPickBlock.isPressed())
                {
                    ;
                }
            }
            else
            {
                while (this.gameSettings.keyBindAttack.isPressed())
                {
                    this.clickMouse();
                }

                while (this.gameSettings.keyBindUseItem.isPressed())
                {
                    this.rightClickMouse();
                }

                while (this.gameSettings.keyBindPickBlock.isPressed())
                {
                    this.middleClickMouse();
                }
            }

            if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem())
            {
                this.rightClickMouse();
            }

            this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
        }

        if (this.theWorld != null)
        {
            if (this.thePlayer != null)
            {
                ++this.joinPlayerCounter;

                if (this.joinPlayerCounter == 30)
                {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
                }
            }

            this.mcProfiler.endStartSection("gameRenderer");

            if (!this.isGamePaused)
            {
                this.entityRenderer.updateRenderer();
            }

            this.mcProfiler.endStartSection("levelRenderer");

            if (!this.isGamePaused)
            {
                this.renderGlobal.updateClouds();
            }

            this.mcProfiler.endStartSection("level");

            if (!this.isGamePaused)
            {
                if (this.theWorld.getLastLightningBolt() > 0)
                {
                    this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
                }

                this.theWorld.updateEntities();
            }
        }
        else if (this.entityRenderer.isShaderActive())
        {
            this.entityRenderer.func_181022_b();
        }

        if (!this.isGamePaused)
        {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
        }

        if (this.theWorld != null)
        {
            if (!this.isGamePaused)
            {
                this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);

                try
                {
                    this.theWorld.tick();
                }
                catch (Throwable throwable2)
                {
                    CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception in world tick");

                    if (this.theWorld == null)
                    {
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
                        crashreportcategory2.addCrashSection("Problem", "Level is null!");
                    }
                    else
                    {
                        this.theWorld.addWorldInfoToCrashReport(crashreport2);
                    }

                    throw new ReportedException(crashreport2);
                }
            }

            this.mcProfiler.endStartSection("animateTick");

            if (!this.isGamePaused && this.theWorld != null)
            {
                this.theWorld.doVoidFogParticles(MathHelper.floor(this.thePlayer.posX), MathHelper.floor(this.thePlayer.posY), MathHelper.floor(this.thePlayer.posZ));
            }

            this.mcProfiler.endStartSection("particles");

            if (!this.isGamePaused)
            {
                this.effectRenderer.updateEffects();
            }
        }
        else if (this.myNetworkManager != null)
        {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReceivedPackets();
        }

        this.mcProfiler.endSection();
        this.systemTime = getSystemTime();
    }

    public void launchIntegratedServer(String folderName, String ingameWorldName, WorldSettings settings)
    {
        loadWorld((WorldClient)null);
        ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && settings != null)
        {
            worldinfo = new WorldInfo(settings, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }

        if (settings == null)
        {
            settings = new WorldSettings(worldinfo);
        }

        try
        {
            this.theIntegratedServer = new IntegratedServer(this, folderName, ingameWorldName, settings);
            this.theIntegratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", ingameWorldName);
            throw new ReportedException(crashreport);
        }

        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));

        while (!this.theIntegratedServer.serverIsInRunLoop())
        {
            String s = this.theIntegratedServer.getUserMessage();

            if (s != null)
            {
                this.loadingScreen.displayLoadingString(I18n.format(s, new Object[0]));
            }
            else
            {
                this.loadingScreen.displayLoadingString("");
            }

            try
            {
                Thread.sleep(200L);
            }
            catch (InterruptedException var9)
            {
                ;
            }
        }

        this.displayGuiScreen((GuiScreen)null);
        SocketAddress socketaddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, (GuiScreen)null));
        networkmanager.sendPacket(new C00Handshake(47, socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
        this.myNetworkManager = networkmanager;
    }

    /**
     * unloads the current world first
     */
    public void loadWorld(WorldClient worldClientIn)
    {
        this.loadWorld(worldClientIn, "");
    }

    /**
     * par2Str is displayed on the loading screen to the user unloads the current world first
     */
    public void loadWorld(WorldClient worldClientIn, String loadingMessage)
    {
        if (worldClientIn == null)
        {
            NetHandlerPlayClient nethandlerplayclient = this.getNetHandler();

            if (nethandlerplayclient != null)
            {
                nethandlerplayclient.cleanup();
            }

            if (this.theIntegratedServer != null && this.theIntegratedServer.isAnvilFileSet())
            {
                this.theIntegratedServer.initiateShutdown();
                this.theIntegratedServer.setStaticInstance();
            }

            this.theIntegratedServer = null;
            this.guiAchievement.clearAchievements();
            this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
        }

        this.renderViewEntity = null;
        this.myNetworkManager = null;

        if (this.loadingScreen != null)
        {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }

        if (worldClientIn == null && this.theWorld != null)
        {
            this.mcResourcePackRepository.func_148529_f();
            this.ingameGUI.func_181029_i();
            this.setServerData((ServerData)null);
            this.integratedServerIsRunning = false;
        }

        this.mcSoundHandler.stopSounds();
        this.theWorld = worldClientIn;

        if (worldClientIn != null)
        {
            if (this.renderGlobal != null)
            {
                this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
            }

            if (this.effectRenderer != null)
            {
                this.effectRenderer.clearEffects(worldClientIn);
            }

            if (this.thePlayer == null)
            {
                this.thePlayer = this.playerController.func_178892_a(worldClientIn, new StatFileWriter());
                this.playerController.flipPlayer(this.thePlayer);
            }

            this.thePlayer.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(this.thePlayer);
            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.thePlayer);
            this.renderViewEntity = this.thePlayer;
        }
        else
        {
            this.saveLoader.flushCache();
            this.thePlayer = null;
        }

        this.systemTime = 0L;
    }

    public void setDimensionAndSpawnPlayer(int dimension)
    {
        this.theWorld.setInitialSpawnLocation();
        this.theWorld.removeAllEntities();
        int i = 0;
        String s = null;

        if (this.thePlayer != null)
        {
            i = this.thePlayer.getEntityId();
            this.theWorld.removeEntity(this.thePlayer);
            s = this.thePlayer.getClientBrand();
        }

        this.renderViewEntity = null;
        EntityPlayerSP entityplayersp = this.thePlayer;
        this.thePlayer = this.playerController.func_178892_a(this.theWorld, this.thePlayer == null ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
        this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(entityplayersp.getDataWatcher().getAllWatched());
        this.thePlayer.dimension = dimension;
        this.renderViewEntity = this.thePlayer;
        this.thePlayer.preparePlayerToSpawn();
        this.thePlayer.setClientBrand(s);
        this.theWorld.spawnEntityInWorld(this.thePlayer);
        this.playerController.flipPlayer(this.thePlayer);
        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.thePlayer.setEntityId(i);
        this.playerController.setPlayerCapabilities(this.thePlayer);
        this.thePlayer.setReducedDebug(entityplayersp.hasReducedDebug());

        if (this.currentScreen instanceof GuiGameOver)
        {
            this.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Gets whether this is a demo or not.
     */
    public final boolean isDemo()
    {
        return this.isDemo;
    }

    public NetHandlerPlayClient getNetHandler()
    {
        return this.thePlayer != null ? this.thePlayer.sendQueue : null;
    }

    public static boolean isGuiEnabled()
    {
        return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled()
    {
        return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
    }

    /**
     * Called when user clicked his mouse middle button (pick block)
     */
    void middleClickMouse()
    {
        if (this.objectMouseOver != null)
        {
            boolean flag = this.thePlayer.capabilities.isCreativeMode;
            int i = 0;
            boolean flag1 = false;
            TileEntity tileentity = null;
            Item item;

            if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos blockpos = objectMouseOver.getBlockPos();
                Block block = theWorld.getBlockState(blockpos).getBlock();

                if (block.getMaterial() == Material.air || (item = block.getItem(theWorld, blockpos)) == null)
                    return;

                if (flag && GuiScreen.isCtrlKeyDown())
                    tileentity = theWorld.getTileEntity(blockpos);

                Block block1 = item instanceof ItemBlock && !block.isFlowerPot() ? Block.getBlockFromItem(item) : block;
                i = block1.getDamageValue(this.theWorld, blockpos);
                flag1 = item.getHasSubtypes();
            }
            else
            {
                if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !flag)
                    return;

                if (this.objectMouseOver.entityHit instanceof EntityPainting)
                    item = Items.painting;
                else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot)
                    item = Items.lead;
                else if (this.objectMouseOver.entityHit instanceof EntityItemFrame)
                {
                    EntityItemFrame itemframe = (EntityItemFrame)objectMouseOver.entityHit;
                    ItemStack is = itemframe.getDisplayedItem();

                    if (is == null)
                        item = Items.item_frame;
                    else
                    {
                        item = is.getItem();
                        i = is.getMetadata();
                        flag1 = true;
                    }
                }
                else if (objectMouseOver.entityHit instanceof EntityMinecart)
                {
                    EnumMinecartType t = ((EntityMinecart)objectMouseOver.entityHit).getMinecartType();
                    item = t == EnumMinecartType.FURNACE ? Items.furnace_minecart : t == EnumMinecartType.CHEST ? Items.chest_minecart : t == EnumMinecartType.TNT ? Items.tnt_minecart :
                    	t == EnumMinecartType.HOPPER ? Items.hopper_minecart : t == EnumMinecartType.COMMAND_BLOCK ? Items.command_block_minecart : Items.minecart;
                }
                else if (this.objectMouseOver.entityHit instanceof EntityBoat)
                    item = Items.boat;
                else if (this.objectMouseOver.entityHit instanceof EntityArmorStand)
                    item = Items.armor_stand;
                else
                {
                    item = Items.spawn_egg;
                    i = EntityList.getEntityID(objectMouseOver.entityHit);
                    flag1 = true;
                    if (!EntityList.entityEggs.containsKey(i))
                        return;
                }
            }

            InventoryPlayer inventoryplayer = this.thePlayer.inventory;

            if (tileentity == null)
            {
                inventoryplayer.setCurrentItem(item, i, flag1, flag);
            }
            else
            {
                ItemStack itemstack1 = this.func_181036_a(item, i, tileentity);
                inventoryplayer.setInventorySlotContents(inventoryplayer.currentItem, itemstack1);
            }

            if (flag)
            {
                int j = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + inventoryplayer.currentItem;
                this.playerController.sendSlotPacket(inventoryplayer.getStackInSlot(inventoryplayer.currentItem), j);
            }
        }
    }

    private ItemStack func_181036_a(Item p_181036_1_, int p_181036_2_, TileEntity p_181036_3_)
    {
        ItemStack itemstack = new ItemStack(p_181036_1_, 1, p_181036_2_);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        p_181036_3_.writeToNBT(nbttagcompound);

        if (p_181036_1_ == Items.skull && nbttagcompound.hasKey("Owner"))
        {
            NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            nbttagcompound3.setTag("SkullOwner", nbttagcompound2);
            itemstack.setTagCompound(nbttagcompound3);
            return itemstack;
        }
        else
        {
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            NBTTagList nbttaglist = new NBTTagList();
            nbttaglist.appendTag(new NBTTagString("(+NBT)"));
            nbttagcompound1.setTag("Lore", nbttaglist);
            itemstack.setTagInfo("display", nbttagcompound1);
            return itemstack;
        }
    }

    /**
     * adds core server Info (GL version , Texture pack, isModded, type), and the worldInfo to the crash report
     */
    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash)
    {
        theCrash.getCategory().addCrashSectionCallable("Launched Version", new Callable<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.this.launchedVersion;
            }
        });
        theCrash.getCategory().addCrashSectionCallable("LWJGL", new Callable<String>()
        {
            public String call()
            {
                return Sys.getVersion();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("OpenGL", new Callable<String>()
        {
            public String call()
            {
                return GL11.glGetString(GL11.GL_RENDERER) + " GL version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR);
            }
        });
        theCrash.getCategory().addCrashSectionCallable("GL Caps", new Callable<String>()
        {
            public String call()
            {
                return OpenGlHelper.getLogText();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Using VBOs", new Callable<String>()
        {
            public String call()
            {
                return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>()
        {
            public String call() throws Exception
            {
                String s = ClientBrandRetriever.getClientModName();
                return !s.equals("vanilla") ? "Definitely; Client brand changed to \'" + s + "\'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.");
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Type", new Callable<String>()
        {
            public String call() throws Exception
            {
                return "Client (map_client.txt)";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Resource Packs", new Callable<String>()
        {
            public String call() throws Exception
            {
                StringBuilder stringbuilder = new StringBuilder();

                for (String s : Minecraft.this.gameSettings.resourcePacks)
                {
                    if (stringbuilder.length() > 0)
                    {
                        stringbuilder.append(", ");
                    }

                    stringbuilder.append(s);

                    if (Minecraft.this.gameSettings.field_183018_l.contains(s))
                    {
                        stringbuilder.append(" (incompatible)");
                    }
                }

                return stringbuilder.toString();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Current Language", new Callable<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
            }
        });
        theCrash.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        theCrash.getCategory().addCrashSectionCallable("CPU", new Callable<String>()
        {
            public String call()
            {
                return OpenGlHelper.func_183029_j();
            }
        });

        if (this.theWorld != null)
        {
            this.theWorld.addWorldInfoToCrashReport(theCrash);
        }

        return theCrash;
    }

    /**
     * Return the singleton Minecraft instance for the game
     */
    public static Minecraft getMinecraft()
    {
        return theMinecraft;
    }

    public ListenableFuture<Object> scheduleResourcesRefresh()
    {
        return this.addScheduledTask(new Runnable()
        {
            public void run()
            {
                Minecraft.this.refreshResources();
            }
        });
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper)
    {
        playerSnooper.addClientStat("fps", Integer.valueOf(debugFPS));
        playerSnooper.addClientStat("vsync_enabled", Boolean.valueOf(this.gameSettings.enableVsync));
        playerSnooper.addClientStat("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
        playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerSnooper.addClientStat("run_time", Long.valueOf((MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        playerSnooper.addClientStat("current_action", this.func_181538_aA());
        String s = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
        playerSnooper.addClientStat("endianness", s);
        playerSnooper.addClientStat("resource_packs", Integer.valueOf(this.mcResourcePackRepository.getRepositoryEntries().size()));
        int i = 0;

        for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries())
        {
            playerSnooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.getResourcePackName());
        }

        if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null)
        {
            playerSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }

    private String func_181538_aA()
    {
        return this.theIntegratedServer != null ? (this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer") : (this.currentServerData != null ? (this.currentServerData.func_181041_d() ? "playing_lan" : "multiplayer") : "out_of_game");
    }

    public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper)
    {
        playerSnooper.addStatToSnooper("opengl_version", GL11.glGetString(GL11.GL_VERSION));
        playerSnooper.addStatToSnooper("opengl_vendor", GL11.glGetString(GL11.GL_VENDOR));
        playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_arrays_of_arrays));
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", Boolean.valueOf(contextcapabilities.GL_ARB_base_instance));
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", Boolean.valueOf(contextcapabilities.GL_ARB_blend_func_extended));
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_clear_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_color_buffer_float));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", Boolean.valueOf(contextcapabilities.GL_ARB_compatibility));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", Boolean.valueOf(contextcapabilities.GL_ARB_compressed_texture_pixel_storage));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_clamp));
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_texture));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers_blend));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_elements_base_vertex));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_indirect));
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_instanced));
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_attrib_location));
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_uniform_location));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_layer_viewport));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program_shadow));
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_sRGB));
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_ARB_geometry_shader4));
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", Boolean.valueOf(contextcapabilities.GL_ARB_gpu_shader5));
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_pixel));
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_vertex));
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_instanced_arrays));
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_alignment));
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_range));
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", Boolean.valueOf(contextcapabilities.GL_ARB_multisample));
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", Boolean.valueOf(contextcapabilities.GL_ARB_multitexture));
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", Boolean.valueOf(contextcapabilities.GL_ARB_occlusion_query2));
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_pixel_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_seamless_cube_map));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_objects));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_stencil_export));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_texture_lod));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow));
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow_ambient));
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", Boolean.valueOf(contextcapabilities.GL_ARB_stencil_texturing));
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", Boolean.valueOf(contextcapabilities.GL_ARB_sync));
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_tessellation_shader));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_border_clamp));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map_array));
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_non_power_of_two));
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_uniform_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_blend));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_program));
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_shader));
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", Boolean.valueOf(contextcapabilities.GL_EXT_bindable_uniform));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_equation_separate));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_func_separate));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_minmax));
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_subtract));
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_EXT_draw_instanced));
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_multisample));
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_object));
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_sRGB));
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_geometry_shader4));
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_program_parameters));
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_shader4));
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", Boolean.valueOf(contextcapabilities.GL_EXT_multi_draw_arrays));
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", Boolean.valueOf(contextcapabilities.GL_EXT_packed_depth_stencil));
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", Boolean.valueOf(contextcapabilities.GL_EXT_paletted_texture));
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", Boolean.valueOf(contextcapabilities.GL_EXT_rescale_normal));
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", Boolean.valueOf(contextcapabilities.GL_EXT_separate_shader_objects));
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", Boolean.valueOf(contextcapabilities.GL_EXT_shader_image_load_store));
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", Boolean.valueOf(contextcapabilities.GL_EXT_shadow_funcs));
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", Boolean.valueOf(contextcapabilities.GL_EXT_shared_texture_palette));
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_clear_tag));
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_two_side));
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_wrap));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_3d));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_array));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_buffer_object));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_integer));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_lod_bias));
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_sRGB));
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_shader));
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_weighting));
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(35071)));
        GL11.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", Integer.valueOf(getGLMaximumTextureSize()));
    }

    /**
     * Used in the usage snooper.
     */
    public static int getGLMaximumTextureSize()
    {
        for (int i = 16384; i > 0; i >>= 1)
        {
            GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, i, i, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)((ByteBuffer)null));
            int j = GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);

            if (j != 0)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return this.gameSettings.snooperEnabled;
    }

    /**
     * Set the current ServerData instance.
     */
    public void setServerData(ServerData serverDataIn)
    {
        this.currentServerData = serverDataIn;
    }

    public ServerData getCurrentServerData()
    {
        return this.currentServerData;
    }

    public boolean isIntegratedServerRunning()
    {
        return this.integratedServerIsRunning;
    }

    /**
     * Returns true if there is only one player playing, and the current server is the integrated one.
     */
    public boolean isSingleplayer()
    {
        return this.integratedServerIsRunning && this.theIntegratedServer != null;
    }

    /**
     * Returns the currently running integrated server
     */
    public IntegratedServer getIntegratedServer()
    {
        return this.theIntegratedServer;
    }

    /**
     * Returns the PlayerUsageSnooper instance.
     */
    public PlayerUsageSnooper getPlayerUsageSnooper()
    {
        return this.usageSnooper;
    }

    /**
     * Gets the system time in milliseconds.
     */
    public static long getSystemTime()
    {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    /**
     * Returns whether we're in full screen or not.
     */
    public boolean isFullScreen()
    {
        return this.fullscreen;
    }

    public Session getSession()
    {
        return this.session;
    }

    public PropertyMap getTwitchDetails()
    {
        return this.twitchDetails;
    }

    public PropertyMap func_181037_M()
    {
        if (this.field_181038_N.isEmpty())
        {
            GameProfile gameprofile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
            this.field_181038_N.putAll(gameprofile.getProperties());
        }

        return this.field_181038_N;
    }

    public Proxy getProxy()
    {
        return this.proxy;
    }

    public TextureManager getTextureManager()
    {
        return this.renderEngine;
    }

    public IResourceManager getResourceManager()
    {
        return this.mcResourceManager;
    }

    public ResourcePackRepository getResourcePackRepository()
    {
        return this.mcResourcePackRepository;
    }

    public LanguageManager getLanguageManager()
    {
        return this.mcLanguageManager;
    }

    public TextureMap getTextureMapBlocks()
    {
        return this.textureMapBlocks;
    }

    public boolean isJava64bit()
    {
        return this.jvm64bit;
    }

    public boolean isGamePaused()
    {
        return this.isGamePaused;
    }

    public SoundHandler getSoundHandler()
    {
        return this.mcSoundHandler;
    }

    public MusicTicker.MusicType getAmbientMusicType()
    {
        return this.thePlayer != null ? (this.thePlayer.worldObj.provider instanceof WorldProviderHell ? MusicTicker.MusicType.NETHER : (this.thePlayer.worldObj.provider instanceof WorldProviderEnd ? (BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : (this.thePlayer.capabilities.isCreativeMode && this.thePlayer.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU;
    }

    public IStream getTwitchStream()
    {
        return this.stream;
    }

    public void dispatchKeypresses()
    {
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();

        if (i != 0 && !Keyboard.isRepeatEvent() && (!(this.currentScreen instanceof GuiControls) || ((GuiControls)currentScreen).time <= getSystemTime() - 20))
        {
            if (Keyboard.getEventKeyState())
            {
                if (i == gameSettings.keyBindStreamStartStop.getKeyCode())
                {
                    if (getTwitchStream().isBroadcasting())
                        getTwitchStream().stopBroadcasting();
                    else if (getTwitchStream().isReadyToBroadcast())
                        displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
                        {
                            public void confirmClicked(boolean result, int id)
                            {
                                if (result)
                                    getTwitchStream().func_152930_t();
                                displayGuiScreen(null);
                            }
                        }, I18n.format("stream.confirm_start"), "", 0));
                    else if (getTwitchStream().func_152928_D() && getTwitchStream().func_152936_l() && theWorld != null)
                        ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
                    else
                        GuiStreamUnavailable.func_152321_a(currentScreen);
                }
                else if (i == gameSettings.keyBindStreamPauseUnpause.getKeyCode())
                {
                    if (getTwitchStream().isBroadcasting())
                    {
                        if (getTwitchStream().isPaused())
                            getTwitchStream().unpause();
                        else
                            getTwitchStream().pause();
                    }
                }
                else if (i == gameSettings.keyBindStreamCommercials.getKeyCode() && getTwitchStream().isBroadcasting())
                    getTwitchStream().requestCommercial();
                else if (i == gameSettings.keyBindStreamToggleMic.getKeyCode())
                    stream.muteMicrophone(true);
                else if (i == gameSettings.keyBindFullscreen.getKeyCode())
                    toggleFullscreen();
                else if (i == gameSettings.keyBindScreenshot.getKeyCode())
                    ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(mcDataDir, displayWidth, displayHeight, framebufferMc));
            }
            else if (i == gameSettings.keyBindStreamToggleMic.getKeyCode())
                this.stream.muteMicrophone(false);
        }
    }

    public MinecraftSessionService getSessionService()
    {
        return sessionService;
    }

    public SkinManager getSkinManager()
    {
        return skinManager;
    }

    public Entity getRenderViewEntity()
    {
        return renderViewEntity;
    }

    public void setRenderViewEntity(Entity viewingEntity)
    {
        renderViewEntity = viewingEntity;
        entityRenderer.loadEntityShader(viewingEntity);
    }

    public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule)
    {
        Validate.notNull(callableToSchedule);

        if (!this.isCallingFromMinecraftThread())
        {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.<V>create(callableToSchedule);

            synchronized (this.scheduledTasks)
            {
                scheduledTasks.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
        else
            try
            {
                return Futures.<V>immediateFuture(callableToSchedule.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedCheckedFuture(exception);
            }
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
    {
        Validate.notNull(runnableToSchedule);
        return this.<Object>addScheduledTask(Executors.callable(runnableToSchedule));
    }

    public boolean isCallingFromMinecraftThread()
    {
        return Thread.currentThread() == mcThread;
    }

    public BlockRendererDispatcher getBlockRendererDispatcher()
    {
        return blockRenderDispatcher;
    }

    public RenderManager getRenderManager()
    {
        return renderManager;
    }

    public RenderItem getRenderItem()
    {
        return renderItem;
    }

    public ItemRenderer getItemRenderer()
    {
        return itemRenderer;
    }

    public static int getDebugFPS()
    {
        return debugFPS;
    }

    public FrameTimer func_181539_aj()
    {
        return field_181542_y;
    }

    public static Map<String, String> getSessionInfo()
    {
        Map<String, String> map = Maps.<String, String>newHashMap();
        map.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
        map.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
        map.put("X-Minecraft-Version", "1.8.8");
        return map;
    }

    public boolean func_181540_al()
    {
        return field_181541_X;
    }

    public void func_181537_a(boolean b)
    {
        this.field_181541_X = b;
    }
}
