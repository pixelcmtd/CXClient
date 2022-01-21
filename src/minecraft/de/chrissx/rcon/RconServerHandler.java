package de.chrissx.rcon;

import de.chrissx.util.Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RconServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		RconPacket packet = RconPacket.parse((ByteBuf) msg);
		System.out.println(packet.length + " " + packet.id + " " + packet.type + " " + packet.payload);
		ctx.writeAndFlush(packet.encode());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		t.printStackTrace();
		ctx.close();
		Util.sendMessage("The RCON server just crashed, please check the log for more details.");
	}

}
