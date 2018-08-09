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
	
	public static String getThisJar() {
		String path = new File("").getAbsoluteFile().getAbsolutePath();
		if(System.getProperty("os.name").toLowerCase().contains("win")) {
			String[] splt = split(path, '\\');
			return path+splt[splt.length-1]+".jar";
		}else {
			String[] splt = split(path, '/');
			return path+splt[splt.length-1]+".jar";
		}
	}
	
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
		
		final byte data[] = new byte[65536];
		int count = 0;
		
		while ((count = in.read(data, 0, 1024)) != -1)
			out.write(data, 0, count);
		
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
		  lore.appendTag(new NBTTagString("by chrissx & Garkolym"));
		  
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
		lore.appendTag(new NBTTagString("by chrissx & Garkolym"));
		  
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
		lore.appendTag(new NBTTagString("by chrissx & Garkolym"));
		
		display.setTag("Lore", lore);
		base.setTag("display", display);
		
		itm.setTagCompound(base);
		itm.setStackDisplayName("use this to enable the blocks");
		
		cheatItem(itm, 38);
	}
	
	public static NBTTagList getEffect(int effect, int amplifier, int duration) {
		NBTTagList l = new NBTTagList();
		
		NBTTagCompound eff = new NBTTagCompound();
	    eff.setInteger("Amplifier", amplifier);
	    eff.setInteger("Duration", duration);
	    eff.setInteger("Id", effect);
	    
	    l.appendTag(eff);

		return l;
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
		mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent("{\"text\":\"" + Consts.prefix + msg + "\"}"));
	}
	
	public static void faceEntity(Entity e) {
		float[] rotations = getRotationsNeeded(e);
		if(rotations != null) {
			mc.thePlayer.rotationYaw = rotations[0];
			mc.thePlayer.rotationPitch = rotations[1]+8.1f;
		}
	}
	
	static float[] getRotationsNeeded(Entity entity) {
        double diffX = entity.posX - mc.thePlayer.posX;
        double diffY;
        if(entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }else
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double diffZ = entity.posZ - mc.thePlayer.posZ;
        return new float[] {mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(((float)(Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90) - mc.thePlayer.rotationYaw),
        		mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(((float)-(Math.atan2(diffY, MathHelper.sqrt(diffX * diffX + diffZ * diffZ)) * 180 / Math.PI)) - mc.thePlayer.rotationPitch)};
    }
	
	public static BlockPos[] getBlocksAround(EntityPlayer p, byte range, boolean mustBeVisible) {
		List<BlockPos> out = new ArrayList<BlockPos>();
		for(int x = (int)p.posX-range; x < p.posX+range; x++)
			for(int y = (int)p.posY-range; y < p.posY+range; y++)
				for(int z = (int)p.posZ-range; z < p.posZ+range; z++) {
					BlockPos bp = new BlockPos(x, y, z);
					if(!mc.theWorld.isAirBlock(bp) && (!mustBeVisible || isBlockVisible(bp)))
						out.add(bp);
				}
		return out.toArray(new BlockPos[out.size()]);
	}
	
	static boolean isBlockVisible(BlockPos p) {
		World w = mc.theWorld;
		
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();
		
		double px = mc.thePlayer.posX;
		double py = mc.thePlayer.posY+mc.thePlayer.getEyeHeight();
		double pz = mc.thePlayer.posZ;
		
		boolean right = w.isAirBlock(new BlockPos(x + 1, y, z));
		boolean back = w.isAirBlock(new BlockPos(x, y, z - 1));
		boolean front = w.isAirBlock(new BlockPos(x, y, z + 1));
		boolean up = w.isAirBlock(new BlockPos(x, y + 1, z));
		boolean down = w.isAirBlock(new BlockPos(x, y - 1, z));
		
		if((up && py > y) || (down && py < y) || (w.isAirBlock(new BlockPos(x-1, y, z)) && px < x) || (right && px > x) || (back && pz < z) || (front && pz > z))
			return true;
		else
			return false;
	}
	
	public static LocFloat64 getEyesPos() {
		return new LocFloat64(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
	}
	
	static float[] getNeededRotations(LocFloat64 vec) {
	    LocFloat64 eyesPos = getEyesPos();
	    
	    double diffX = vec.x - eyesPos.x;
	    double diffY = vec.y - eyesPos.y;
	    double diffZ = vec.z - eyesPos.z;
	    
	    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
	    
	    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
	    float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
	    
	    return new float[] {MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
	}
	
	public static String randomSortString(String originalString) {
		char[] chars = originalString.toCharArray();
		StringBuilder s = new StringBuilder();
		List<Integer> used = new ArrayList<Integer>();
		for(int i = 0; i < chars.length; i++) {
			int a = Random.rand.nextInt(chars.length);
			while(used.contains(a))
				a = Random.rand.nextInt(chars.length);
			s.append(chars[a]);
			used.add(a);
		}
		return s.toString();
	}
}