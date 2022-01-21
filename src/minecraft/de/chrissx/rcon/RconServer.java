package de.chrissx.rcon;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RconServer {

	public static ChannelFuture makeRconServer() {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new NioEventLoopGroup());
		serverBootstrap.channel(NioServerSocketChannel.class);
		// TODO: we might not want to fuck up minecraft servers with rcon running on the same machine
		serverBootstrap.localAddress(new InetSocketAddress("localhost", 25575));
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new RconServerHandler());
			}
		});
		return serverBootstrap.bind();
	}

}
