package net.minecraft.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;
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
        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.accepts("demo");
        parser.accepts("fullscreen");
        parser.accepts("checkGlErrors");
        OptionSpec<String> server = parser.accepts("server").withRequiredArg();
        OptionSpec<Integer> port = parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(25565), new Integer[0]);
        OptionSpec<File> gameDir = parser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
        OptionSpec<File> assetsDir = parser.accepts("assetsDir").withRequiredArg().<File>ofType(File.class);
        OptionSpec<File> resourcePackDir = parser.accepts("resourcePackDir").withRequiredArg().<File>ofType(File.class);
        OptionSpec<String> proxyHost = parser.accepts("proxyHost").withRequiredArg();
        OptionSpec<Integer> proxyPort = parser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).<Integer>ofType(Integer.class);
        OptionSpec<String> proxyUser = parser.accepts("proxyUser").withRequiredArg();
        OptionSpec<String> proxyPass = parser.accepts("proxyPass").withRequiredArg();
        OptionSpec<String> username = parser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L, new String[0]);
        OptionSpec<String> uuid = parser.accepts("uuid").withRequiredArg();
        OptionSpec<String> accessToken = parser.accepts("accessToken").withRequiredArg().required();
        OptionSpec<String> version = parser.accepts("version").withRequiredArg().required();
        OptionSpec<Integer> width = parser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(854), new Integer[0]);
        OptionSpec<Integer> height = parser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(480), new Integer[0]);
        OptionSpec<String> userProperties = parser.accepts("userProperties").withRequiredArg().defaultsTo("{}", new String[0]);
        OptionSpec<String> profileProperties = parser.accepts("profileProperties").withRequiredArg().defaultsTo("{}", new String[0]);
        OptionSpec<String> assetIndex = parser.accepts("assetIndex").withRequiredArg();
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
                proxy = new Proxy(Type.SOCKS, new InetSocketAddress(s, ((Integer)parsedArgs.valueOf(proxyPort)).intValue()));
            }
            catch (Exception var46)
            {
                ;
            }

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

        int i = ((Integer)parsedArgs.valueOf(width)).intValue();
        int j = ((Integer)parsedArgs.valueOf(height)).intValue();
        boolean fullscreen = parsedArgs.has("fullscreen");
        boolean checkGlErrors = parsedArgs.has("checkGlErrors");
        boolean demo = parsedArgs.has("demo");
        String s3 = (String)parsedArgs.valueOf(version);
        Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new Serializer()).create();
        PropertyMap propertymap = (PropertyMap)gson.fromJson((String)parsedArgs.valueOf(userProperties), PropertyMap.class);
        PropertyMap propertymap1 = (PropertyMap)gson.fromJson((String)parsedArgs.valueOf(profileProperties), PropertyMap.class);
        File file1 = (File)parsedArgs.valueOf(gameDir);
        File file2 = parsedArgs.has(assetsDir) ? (File)parsedArgs.valueOf(assetsDir) : new File(file1, "assets/");
        File file3 = parsedArgs.has(resourcePackDir) ? (File)parsedArgs.valueOf(resourcePackDir) : new File(file1, "resourcepacks/");
        String s4 = parsedArgs.has(uuid) ? (String)uuid.value(parsedArgs) : (String)username.value(parsedArgs);
        String s5 = parsedArgs.has(assetIndex) ? (String)assetIndex.value(parsedArgs) : null;
        String s6 = (String)parsedArgs.valueOf(server);
        Integer integer = (Integer)parsedArgs.valueOf(port);
        Session session = new Session((String)username.value(parsedArgs), s4, (String)accessToken.value(parsedArgs), (String)userType.value(parsedArgs));
        GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, propertymap1, proxy), new GameConfiguration.DisplayInformation(i, j, fullscreen, checkGlErrors), new GameConfiguration.FolderInformation(file1, file3, file2, s5), new GameConfiguration.GameInformation(demo, s3), new GameConfiguration.ServerInformation(s6, integer.intValue()));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread")
        {
            public void run()
            {
            	Minecraft m;
            	IntegratedServer s;
            	if ((m = Minecraft.getMinecraft()) != null && (s = m.getIntegratedServer()) != null)
            		s.stopServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        new Minecraft(gameconfiguration).run();
    }

    static boolean notNullOrEmpty(String s)
    {
        return s != null && s != "";
    }
}
