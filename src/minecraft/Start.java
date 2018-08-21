import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import de.chrissx.HackedClient;
import de.chrissx.util.Consts;
import net.minecraft.client.main.Main;

public class Start
{
    public static void main(String[] args)
    {
        Main.main(concat(new String[] {"--version", Consts.clientName.toLowerCase(), "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
