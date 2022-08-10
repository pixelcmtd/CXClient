package de.chrissx.rcon;

import java.net.InetSocketAddress;

import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.minecraft.client.Minecraft;

public class RconServer extends ChannelInboundHandlerAdapter {

	public static ChannelFuture makeRconServer() {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new NioEventLoopGroup());
		serverBootstrap.channel(NioServerSocketChannel.class);
		// TODO: we might not want to fuck up minecraft servers with rcon running on the same machine
		serverBootstrap.localAddress(new InetSocketAddress("localhost", 25575));
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new RconServer());
			}
		});
		return serverBootstrap.bind();
	}

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
