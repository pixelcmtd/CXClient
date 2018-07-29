package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;

public class Util
{
    public static Util.EnumOS getOSType()
    {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? Util.EnumOS.WINDOWS : (s.contains("mac") ? Util.EnumOS.OSX : (s.contains("solaris") ? Util.EnumOS.SOLARIS : (s.contains("sunos") ? Util.EnumOS.SOLARIS : (s.contains("linux") || s.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN))));
    }

    public static <V> V runFutureTask(FutureTask<V> futuretask, Logger logger)
    {
        try
        {
            futuretask.run();
            return futuretask.get();
        }
        catch (ExecutionException e)
        {
            logger.fatal((String)"Error executing task", (Throwable)e);
        }
        catch (InterruptedException e)
        {
            logger.fatal((String)"Error executing task", (Throwable)e);
        }

        return (V)null;
    }

    public static enum EnumOS
    {
        LINUX,
        SOLARIS,
        WINDOWS,
        OSX,
        UNKNOWN;
    }
}
