package net.minecraft.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

public class CompressedStreamTools
{
    /**
     * Load the gzipped compound from the inputstream.
     */
    public static NBTTagCompound readCompressed(InputStream is) throws IOException
    {
        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(is)));
        NBTTagCompound nbttagcompound;

        try
        {
            nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
        }
        finally
        {
            datainputstream.close();
        }

        return nbttagcompound;
    }

    /**
     * Write the compound, gzipped, to the outputstream.
     */
    public static void writeCompressed(NBTTagCompound nbt, OutputStream os) throws IOException
    {
        DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(os)));

        try
        {
            write(nbt, dataoutputstream);
        }
        finally
        {
            dataoutputstream.close();
        }
    }

    public static void safeWrite(NBTTagCompound nbt, File f) throws IOException
    {
        File file1 = new File(f.getAbsolutePath() + "_tmp");

        if (file1.exists())
            file1.delete();

        write(nbt, file1);

        if (f.exists())
            f.delete();

        if (f.exists())
            throw new IOException("Failed to delete " + f);
        else
            file1.renameTo(f);
    }

    public static void write(NBTTagCompound nbt, File f) throws IOException
    {
        DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(f));

        try
        {
            write(nbt, dataoutputstream);
        }
        finally
        {
            dataoutputstream.close();
        }
    }

    public static NBTTagCompound read(File f) throws IOException
    {
        if (!f.exists())
            return null;
        else
        {
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(f));
            NBTTagCompound nbttagcompound;

            try
            {
                nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
            }
            finally
            {
                datainputstream.close();
            }

            return nbttagcompound;
        }
    }

    /**
     * Reads from a CompressedStream.
     */
    public static NBTTagCompound read(DataInputStream is) throws IOException
    {
        return read(is, NBTSizeTracker.INFINITE);
    }

    /**
     * Reads the given DataInput, constructs, and returns an NBTTagCompound with the data from the DataInput
     */
    public static NBTTagCompound read(DataInput di, NBTSizeTracker nbtst) throws IOException
    {
        NBTBase nbtbase = func_152455_a(di, 0, nbtst);

        if (nbtbase instanceof NBTTagCompound)
        {
            return (NBTTagCompound)nbtbase;
        }
        else
        {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(NBTTagCompound nbt, DataOutput dop) throws IOException
    {
        writeTag(nbt, dop);
    }

    private static void writeTag(NBTBase nbt, DataOutput dop) throws IOException
    {
        dop.writeByte(nbt.getId());

        if (nbt.getId() != 0)
        {
            dop.writeUTF("");
            nbt.write(dop);
        }
    }

    private static NBTBase func_152455_a(DataInput p_152455_0_, int p_152455_1_, NBTSizeTracker p_152455_2_) throws IOException
    {
        byte b0 = p_152455_0_.readByte();

        if (b0 == 0)
        {
            return new NBTTagEnd();
        }
        else
        {
            p_152455_0_.readUTF();
            NBTBase nbtbase = NBTBase.createNewByType(b0);

            try
            {
                nbtbase.read(p_152455_0_, p_152455_1_, p_152455_2_);
                return nbtbase;
            }
            catch (IOException ioexception)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
                crashreportcategory.addCrashSection("Tag name", "[UNNAMED TAG]");
                crashreportcategory.addCrashSection("Tag type", Byte.valueOf(b0));
                throw new ReportedException(crashreport);
            }
        }
    }
}
