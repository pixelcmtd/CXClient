package de.chrissx.rcon;

import de.chrissx.util.Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.client.Minecraft;

public class RconServerHandler extends ChannelInboundHandlerAdapter {

	boolean authenticated = false;

	// TODO: somehow support C->S fragmentation
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		RconPacket packet = RconPacket.parse((ByteBuf) msg);
		Minecraft.logger.info("[c/RCON] Received packet " + packet);
		if(packet.type == 3) {
			// TODO: global for rcon password
			if(packet.payload == "penis1234") {
				authenticated = true;
				ctx.writeAndFlush(new RconPacket(packet.id, 2, ""));
			} else {
				authenticated = false;
				ctx.writeAndFlush(new RconPacket(-1, 2, ""));
			}
		} else if(packet.type == 2 && authenticated) {
			// TODO: run command (support S->C fragmentation!)
			ctx.writeAndFlush((new RconPacket(packet.id, 0, "")));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		Util.sendMessage("The RCON server just crashed, please check the log for more details.");
		t.printStackTrace();
		ctx.close();
	}

}
