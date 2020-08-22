package net.minecraft.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;

import de.chrissx.util.Consts;

import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Session;

public class Main
{
    public static void main(String[] args)
    {
        Thread.currentThread().setName("Client thread");
        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.accepts("demo");
        parser.accepts("fullscreen");
        parser.accepts("checkGlErrors");
        OptionSpec<String> server = parser.accepts("server").withRequiredArg();
        OptionSpec<Integer> port = parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
        OptionSpec<File> gameDir = parser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        OptionSpec<File> assetsDir = parser.accepts("assetsDir").withRequiredArg().defaultsTo("assets").<File>ofType(File.class);
        OptionSpec<File> resourcePackDir = parser.accepts("resourcePackDir").withRequiredArg().<File>ofType(File.class);
        OptionSpec<String> proxyHost = parser.accepts("proxyHost").withRequiredArg();
        OptionSpec<Integer> proxyPort = parser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).<Integer>ofType(Integer.class);
        OptionSpec<String> proxyUser = parser.accepts("proxyUser").withRequiredArg();
        OptionSpec<String> proxyPass = parser.accepts("proxyPass").withRequiredArg();
        OptionSpec<String> username = parser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L);
        OptionSpec<String> uuid = parser.accepts("uuid").withRequiredArg();
        OptionSpec<String> accessToken = parser.accepts("accessToken").withRequiredArg().defaultsTo("0");
        OptionSpec<String> version = parser.accepts("version").withRequiredArg().defaultsTo(Consts.clientName.toLowerCase() + "_" + Consts.version);
        OptionSpec<Integer> w = parser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        OptionSpec<Integer> h = parser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        OptionSpec<String> userProperties = parser.accepts("userProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> profileProperties = parser.accepts("profileProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> assetIndex = parser.accepts("assetIndex").withRequiredArg().defaultsTo("1.8");
        OptionSpec<String> userType = parser.accepts("userType").withRequiredArg().defaultsTo("legacy", new String[0]);
        OptionSpec<String> nonOptions = parser.nonOptions();
        OptionSet parsedArgs = parser.parse(args);
        List<String> list = parsedArgs.valuesOf(nonOptions);

        if (!list.isEmpty())
            System.out.println("Completely ignored arguments: " + list);

        String s = (String)parsedArgs.valueOf(proxyHost);
        Proxy proxy = Proxy.NO_PROXY;

        if (s != null)
            try
            {
                proxy = new Proxy(Type.SOCKS, new InetSocketAddress(s, parsedArgs.valueOf(proxyPort)));
            }
            catch (Exception var46) {}

        final String s1 = (String)parsedArgs.valueOf(proxyUser);
        final String s2 = (String)parsedArgs.valueOf(proxyPass);

        if (!proxy.equals(Proxy.NO_PROXY) && notNullOrEmpty(s1) && notNullOrEmpty(s2))
        {
            Authenticator.setDefault(new Authenticator()
            {
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(s1, s2.toCharArray());
                }
            });
        }

        int i = parsedArgs.valueOf(w);
        int j = parsedArgs.valueOf(h);
        boolean fs = parsedArgs.has("fullscreen");
        boolean checkGlErrors = parsedArgs.has("checkGlErrors");
        boolean demo = parsedArgs.has("demo");
        String s3 = (String)parsedArgs.valueOf(version);
        Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new Serializer()).create();
        PropertyMap propertymap = (PropertyMap)gson.fromJson((String)parsedArgs.valueOf(userProperties), PropertyMap.class);
        PropertyMap propertymap1 = (PropertyMap)gson.fromJson((String)parsedArgs.valueOf(profileProperties), PropertyMap.class);
        File f = parsedArgs.valueOf(gameDir);
        Consts.init(f);
        File g = parsedArgs.has(assetsDir) ? (File)parsedArgs.valueOf(assetsDir) : new File(f, "assets/");
        File k = parsedArgs.has(resourcePackDir) ? (File)parsedArgs.valueOf(resourcePackDir) : new File(f, "resourcepacks/");
        String s4 = parsedArgs.has(uuid) ? (String)uuid.value(parsedArgs) : (String)username.value(parsedArgs);
        String s5 = parsedArgs.has(assetIndex) ? (String)assetIndex.value(parsedArgs) : null;
        String s6 = (String)parsedArgs.valueOf(server);
        int portValue = parsedArgs.valueOf(port);
        Session session = new Session((String)username.value(parsedArgs), s4, (String)accessToken.value(parsedArgs), (String)userType.value(parsedArgs));
        GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, propertymap1, proxy), new GameConfiguration.DisplayInformation(i, j, fs, checkGlErrors), new GameConfiguration.FolderInformation(f, k, g, s5), new GameConfiguration.GameInformation(demo, s3), new GameConfiguration.ServerInformation(s6, portValue));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread")
        {
            public void run()
            {
            	Minecraft m = Minecraft.getMinecraft();
            	if(m == null) return;
            	IntegratedServer s = m.getIntegratedServer();
            	if(s == null) return;
            	s.stopServer();
            }
        });
        new Minecraft(gameconfiguration).run();
    }

    static boolean notNullOrEmpty(String s)
    {
        return s != null && s != "";
    }
}
