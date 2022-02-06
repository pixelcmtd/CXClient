package de.chrissx.rcon;

import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.client.Minecraft;

public class RconServerHandler extends ChannelInboundHandlerAdapter {

	boolean authenticated = false;

	RconPacket processPacket(RconPacket packet) {
		if(packet.type == 3) {
			// TODO: global for rcon password
			if(packet.payload.equals("penis1234")) {
				authenticated = true;
				return new RconPacket(packet.id, 2, "");
			} else {
				authenticated = false;
				return new RconPacket(-1, 2, "");
			}
		} else if(packet.type == 2 && authenticated) {
			// TODO: run command (support S->C fragmentation!)
			return new RconPacket(packet.id, 0, "");
		} else {
			return null;
		}
	}

	// TODO: somehow support C->S fragmentation
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		Thread.currentThread().setName(Consts.clientName + " RCON thread");
		RconPacket req = RconPacket.parse((ByteBuf) msg);
		Minecraft.logger.info("Received packet " + req);
		RconPacket res = processPacket(req);
		if(res != null) {
			Minecraft.logger.info("Answering: " + res);
			ctx.writeAndFlush(res.encode());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		Thread.currentThread().setName(Consts.clientName + " RCON thread");
		Util.sendMessage("The RCON server just crashed, please check the log for more details.");
		t.printStackTrace();
		ctx.close();
	}

}
