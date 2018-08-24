package de.chrissx.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.chrissx.locations.LocFloat64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Util {

	static final Minecraft mc = Minecraft.getMinecraft();

	public static void checkIfExistsAndMake(String dir, String name)
	{
		File f = new File(dir);
		if(!(f.exists()))
		{
			f.mkdirs();
			mc.logger.info("Made " + name + ".");
		}
	}
	
	public static String combineParts(String[] strings, int startIndex, String seperator) {
		if(startIndex >= strings.length)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(strings[startIndex]);
		for(int i = startIndex + 1; i < strings.length; i++) {
			sb.append(seperator);
			sb.append(strings[i]);
		}
		return sb.toString();
	}
	
	public static BufferedImage scale(BufferedImage src, int w, int h) {
		BufferedImage i = new BufferedImage(w, h, 0x1); // type_int_rgb
	    for (int x = 0; x < w; x++)
	        for (int y = 0; y < h; y++)
	            i.setRGB(x, y, src.getRGB(x * src.getWidth() / w, y * src.getHeight() / h));
	    return i;
	}
	
	//removed getThisJar(), we'll need a new one that works better
	
	/**
	 * Splits the string without trying something with regexes but just the single character separator.
	 * @param s The string to split
	 * @param separator The separator at which the string should be splitted.
	 * @return The splitted string (as an array).
	 */
	public static String[] split(String s, char separator) {
		int i = 0;
		for(char c : s.toCharArray())
			if(c == separator)
				i++;
		String[] split = new String[i];
		int j = 0;
		int l = 0;
		for(int k = 0; k < s.length(); k++)
			if(s.charAt(k) == separator) {
				split[j] = s.substring(l, k);
				j++;
				l = k + 1;
			}
		return split;
	}
	
	public static void swapSlots(int slot1, int slot2, int windowId) {
		mc.playerController.windowClick(windowId, slot1, 0, 1, mc.thePlayer);
		mc.playerController.windowClick(windowId, slot2, 0, 1, mc.thePlayer);
	}
	
	/**
	 * Sends the packet to break the block instantly.
	 * @param block The block to break.
	 */
	public static void breakBlock(BlockPos block) {
		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, block, EnumFacing.UP));
		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, block, EnumFacing.UP));
	}
	
	public static String generateTempFile(String tmp, String name, String ext) {
		String out = Paths.get(tmp, name+"_"+Random.rand.nextInt()+ext).toString();
		File f = new File(out);
		while(f.exists())
			f = new File(out = Paths.get(tmp, name+"_"+Random.rand.nextInt()+ext).toString());
		return out;
	}
	
	public static void downloadFile(final String url, final String file) throws IOException {
		final BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
		final FileOutputStream out = new FileOutputStream(file);
		
		final byte[] bfr = new byte[512];
		int count = 0;

		while ((count = in.read(bfr, 0, 65536)) != -1)
			out.write(bfr, 0, count);
		
		out.close();
		in.close();
	}
	
	public static void cheatArmorStand(String msg, double x, double y, double z, int slot) {
		ItemStack itm = new ItemStack(Items.armor_stand);
	    
	    NBTTagCompound base = new NBTTagCompound();
	    NBTTagCompound entityTag = new NBTTagCompound();
	    
	    entityTag.setInteger("Invisible", 1);
	    entityTag.setString("CustomName", msg);
	    entityTag.setInteger("CustomNameVisible", 1);
	    entityTag.setInteger("NoGravity", 1);
	    
	    NBTTagList position = new NBTTagList();
	    
	    position.appendTag(new NBTTagDouble(x));
	    position.appendTag(new NBTTagDouble(y));
	    position.appendTag(new NBTTagDouble(z));
	    
	    entityTag.setTag("Pos", position);
	    
	    base.setTag("EntityTag", entityTag);
	    itm.setTagCompound(base);
	    
	    itm.setStackDisplayName(msg);
	    
	    cheatItem(itm, slot);
	  }
	  
	  public static void cheatCmdBlock(String command) {
		  ItemStack itm = new ItemStack(Blocks.command_block);
		  
		  NBTTagCompound base = new NBTTagCompound();
		  NBTTagCompound entityTag = new NBTTagCompound();
		  NBTTagCompound display = new NBTTagCompound();
		  NBTTagList lore = new NBTTagList();
		  
		  entityTag.setString("Command", "gamerule commandBlockOutput false");
		  entityTag.setString("CustomName", "CXClient");
		  entityTag.setBoolean("TrackOutput", false);
		  
		  lore.appendTag(new NBTTagString("gamerule commandBlockOutput false"));
		  lore.appendTag(new NBTTagString("created using " + Consts.clientName));
		  lore.appendTag(new NBTTagString("by chrissx"));
		  
		  display.setTag("Lore", lore);
		  base.setTag("BlockEntityTag", entityTag);
		  base.setTag("display", display);
		  
		  itm.setTagCompound(base);
		  itm.setStackDisplayName("output disabler");
		  
		  cheatItem(itm, 36);
		  
		  cheatCmd(command);
		  
		  cheatLever();
	}
	  
	static void cheatCmd(String cmd) {
		ItemStack itm = new ItemStack(Blocks.command_block);
		
		NBTTagCompound base = new NBTTagCompound();
		NBTTagCompound entityTag = new NBTTagCompound();
		NBTTagCompound display = new NBTTagCompound();
		NBTTagList lore = new NBTTagList();
		  
		entityTag.setString("Command", cmd);
		entityTag.setString("CustomName", "CXClient");
		entityTag.setBoolean("TrackOutput", false);
		
		lore.appendTag(new NBTTagString(cmd));
		lore.appendTag(new NBTTagString("created using " + Consts.clientName));
		lore.appendTag(new NBTTagString("by chrissx"));
		  
		display.setTag("Lore", lore);
		base.setTag("BlockEntityTag", entityTag);
		base.setTag("display", display);
		  
		itm.setTagCompound(base);
		itm.setStackDisplayName(Consts.clientName);
		
		cheatItem(itm, 37);
	}
	
	static void cheatLever() {
		ItemStack itm = new ItemStack(Blocks.lever);
		
		NBTTagCompound base = new NBTTagCompound();
		NBTTagCompound display = new NBTTagCompound();
		NBTTagList lore = new NBTTagList();
		
		lore.appendTag(new NBTTagString("created using " + Consts.clientName));
		lore.appendTag(new NBTTagString("by chrissx"));
		
		display.setTag("Lore", lore);
		base.setTag("display", display);
		
		itm.setTagCompound(base);
		itm.setStackDisplayName("use this to enable the blocks");
		
		cheatItem(itm, 38);
	}
	
	public static NBTTagList newEffects() {
		return new NBTTagList();
	}

	public static NBTTagList addEffect(NBTTagList effects, int effect, int amplifier, int duration) {
		NBTTagCompound eff = new NBTTagCompound();
	    eff.setInteger("Amplifier", amplifier);
	    eff.setInteger("Duration", duration);
	    eff.setInteger("Id", effect);
	    effects.appendTag(eff);
		return effects;
	}

	public static ItemStack getCustomPotion(NBTTagList effects, String name) {
		ItemStack i = new ItemStack(Items.potionitem);
		i.setItemDamage(16384);
		
		i.setTagInfo("CustomPotionEffects", effects);
		i.setStackDisplayName(name);
		
		return i;
	}

	public static void cheatItem(ItemStack itm, int slot) {
		mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(slot, itm));
	}

	public static void sendChat(String msg) {
		mc.thePlayer.sendChatMessage(msg);
	}

	public static void sendMessage(String msg) {
		mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent("{\"text\":\"" + Consts.prefix + msg.replaceAll("\"", "\\\"") + "\"}"));
	}

	public static void faceEntity(Entity e) {
		float[] rotations = getRotationsNeeded(e);
		if(rotations != null) {
			mc.thePlayer.rotationYaw = rotations[0];
			mc.thePlayer.rotationPitch = rotations[1] + 8.1f;
		}
	}

	static float[] getRotationsNeeded(Entity e) {
        double x = e.posX - mc.thePlayer.posX;
        double y;
        if(e instanceof EntityLivingBase)
        {
            EntityLivingBase elb = (EntityLivingBase)e;
            y = elb.posY + elb.getEyeHeight() * 0.9 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }else
            y = (e.boundingBox.minY + e.boundingBox.maxY) / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = e.posZ - mc.thePlayer.posZ;
        return new float[] {mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(((float)(Math.atan2(z, x) * 180 / Math.PI) - 90) - mc.thePlayer.rotationYaw),
        		mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(((float)-(Math.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180 / Math.PI)) - mc.thePlayer.rotationPitch)};
    }

	public static BlockPos[] getBlocksAround(EntityPlayer player, int range, boolean mustBeVisible) {
		long i = (long)player.posX;
		long j = (long)player.posY;
		long k = (long)player.posZ;
		long l = i + range;
		long m = j + range;
		long n = k + range;
		long o = i - range;
		long p = j - range;
		long q = k - range;
		List<BlockPos> b = new ArrayList<BlockPos>();
		for(long x = (long)o; x < l; x++)
			for(long y = (long)p; y < m; y++)
				for(long z = (long)q; z < n; z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if(!mc.theWorld.isAirBlock(pos) && (!mustBeVisible || isBlockVisible(pos)))
						b.add(pos);
				}
		return b.toArray(new BlockPos[b.size()]);
	}

	static boolean isBlockVisible(BlockPos pos) {
		World w = mc.theWorld;
		EntityPlayerSP p = mc.thePlayer;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		double d = p.posX;
		double e = p.posY + p.getEyeHeight();
		double f = p.posZ;
		boolean b = w.isAirBlock(new BlockPos(i - 1, j, k));
		boolean c = w.isAirBlock(new BlockPos(i + 1, j, k));
		boolean g = w.isAirBlock(new BlockPos(i, j, k - 1));
		boolean h = w.isAirBlock(new BlockPos(i, j, k + 1));
		boolean l = w.isAirBlock(new BlockPos(i, j + 1, k));
		boolean m = w.isAirBlock(new BlockPos(i, j - 1, k));
		if((l && e > j) || (m && e < j) || (b && d < i) || (c && d > i) || (g && f < k) || (h && f > k))
			return true;
		else
			return false;
	}
	
	public static LocFloat64 getEyePos() {
		return new LocFloat64(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
	}
	
	static float[] getNeededRotations(LocFloat64 vec) {
	    double x = vec.x - mc.thePlayer.posX;
	    double y = vec.y - mc.thePlayer.posY - mc.thePlayer.getEyeHeight();
	    double z = vec.z - mc.thePlayer.posZ;
	    
	    return new float[] {MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(z, x)) - 90),
	    		MathHelper.wrapAngleTo180_float((float)-Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z))))};
	}
	
	public static String randomSortString(String originalString) {
		char[] chars = originalString.toCharArray();
		StringBuilder s = new StringBuilder();
		List<Integer> used = new ArrayList<Integer>();
		for(int i = 0; i < chars.length; i++) {
			int j = Random.rand(chars.length);
			while(used.contains(j))
				j = Random.rand(chars.length);
			s.append(chars[j]);
			used.add(j);
		}
		return s.toString();
	}
}