package net.minecraft.network;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import de.chrissx.HackedClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;

public class NetworkManager extends SimpleChannelInboundHandler<Packet>
{
    private static final Logger logger = LogManager.getLogger();
    public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
    public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
    public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = AttributeKey.<EnumConnectionState>valueOf("protocol");
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>()
    {
        protected NioEventLoopGroup load()
        {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> field_181125_e = new LazyLoadBase<EpollEventLoopGroup>()
    {
        protected EpollEventLoopGroup load()
        {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>()
    {
        protected LocalEventLoopGroup load()
        {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
        }
    };
    final EnumPacketDirection direction;
    final Queue<NetworkManager.InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.<NetworkManager.InboundHandlerTuplePacketListener>newConcurrentLinkedQueue();
    final ReentrantReadWriteLock field_181680_j = new ReentrantReadWriteLock();

    /** The active channel */
    private Channel channel;

    /** The address of the remote party */
    private SocketAddress socketAddress;

    /** The INetHandler instance responsible for processing received packets */
    private INetHandler packetListener;

    /** A String indicating why the network has shutdown. */
    private IChatComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;

    public NetworkManager(EnumPacketDirection packetDirection)
    {
        this.direction = packetDirection;
    }

    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception
    {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();

        try
        {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        }
        catch (Throwable throwable)
        {
            logger.fatal((Object)throwable);
        }
    }

    /**
     * Sets the new connection state and registers which packets this channel may send and receive
     */
    public void setConnectionState(EnumConnectionState newState)
    {
        this.channel.attr(attrKeyConnectionState).set(newState);
        this.channel.config().setAutoRead(true);
        logger.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception
    {
        closeChannel(new ChatComponentTranslation("disconnect.endOfStream"));
    }

    public void exceptionCaught(ChannelHandlerContext channelHandler, Throwable caughtException) throws Exception
    {
        ChatComponentTranslation cct;
        caughtException.printStackTrace();

        if (caughtException instanceof TimeoutException)
        {
            cct = new ChatComponentTranslation("disconnect.timeout");
        }
        else
        {
            cct = new ChatComponentTranslation("disconnect.genericReason", "Internal Exception: " + caughtException);
        }

        closeChannel(cct);
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) throws Exception
    {
        if (this.channel.isOpen())
        {
            try
            {
                p_channelRead0_2_.processPacket(this.packetListener);
            }
            catch (ThreadQuickExitException var4) {}
        }
    }

    /**
     * Sets the NetHandler for this NetworkManager, no checks are made if this handler is suitable for the particular
     * connection state (protocol)
     */
    public void setNetHandler(INetHandler handler)
    {
        Validate.notNull(handler, "packetListener", new Object[0]);
        logger.debug("Set listener of {} to {}", this, handler);
        this.packetListener = handler;
    }

    public void sendPacket(Packet packetIn)
    {
        if (this.isChannelOpen())
        {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
        }
        else
        {
            this.field_181680_j.writeLock().lock();

            try
            {
                this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[])null));
            }
            finally
            {
                this.field_181680_j.writeLock().unlock();
            }
        }
    }

    public void sendPacket(Packet packetIn, GenericFutureListener <? extends Future <? super Void >> listener, GenericFutureListener <? extends Future <? super Void >> ... listeners)
    {
        if (this.isChannelOpen())
        {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, ArrayUtils.add(listeners, 0, listener));
        }
        else
        {
            this.field_181680_j.writeLock().lock();

            try
            {
                this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[])ArrayUtils.add(listeners, 0, listener)));
            }
            finally
            {
                this.field_181680_j.writeLock().unlock();
            }
        }
    }
    
    class StoppedPacket
    {
    	public final Packet packet;
    	public final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
    	
    	public StoppedPacket(Packet p, GenericFutureListener<? extends Future<? super Void>>[] fl)
    	{
    		packet = p;
    		futureListeners = fl;
    	}
    }

    List<StoppedPacket> stoppedPackets = new ArrayList<StoppedPacket>();
    boolean testStoppedPackets = true;

    public void printStoppedPackets()
    {
    	for(StoppedPacket sp : stoppedPackets)
    		logger.info(sp.packet);
    }

    /**
     * Will commit the packet to the channel. If the current thread 'owns' the channel it will write and flush the
     * packet, otherwise it will add a task for the channel eventloop thread to do that.
     */
    void dispatchPacket(final Packet inPacket, final GenericFutureListener <? extends Future <? super Void >> [] fl)
    {
    	if(HackedClient.getClient().getMods().lag.isEnabled())
       	{
    		StoppedPacket p = null;
    		if(inPacket instanceof C04PacketPlayerPosition)
    			for(StoppedPacket sp : stoppedPackets)
    				if(sp.packet instanceof C04PacketPlayerPosition)
    					p = sp;
    		if(inPacket instanceof C06PacketPlayerPosLook)
    			for(StoppedPacket sp : stoppedPackets)
    				if(sp.packet instanceof C06PacketPlayerPosLook)
    					p = sp;
    		if(p != null)
    			stoppedPackets.remove(p);
       		stoppedPackets.add(new StoppedPacket(inPacket, fl));
       		return;
       	}
       	else if(stoppedPackets.size() > 0 && testStoppedPackets)
        {
       		StoppedPacket p = stoppedPackets.remove(0);
       		testStoppedPackets = false;
        	dispatchPacket(p.packet, p.futureListeners);
        	testStoppedPackets = true;
        }
        final EnumConnectionState cs1 = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState cs2 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();

        if (cs2 != cs1)
        {
            logger.debug("Disabled auto read");
            channel.config().setAutoRead(false);
        }

        if (channel.eventLoop().inEventLoop())
        {
            if (cs1 != cs2)
                setConnectionState(cs1);

            ChannelFuture cf = channel.writeAndFlush(inPacket);

            if (fl != null)
                cf.addListeners(fl);

            cf.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
        else
        {
            channel.eventLoop().execute(new Runnable()
            {
                public void run()
                {
                    if (cs1 != cs2)
                        setConnectionState(cs1);

                    ChannelFuture cf = channel.writeAndFlush(inPacket);

                    if (fl != null)
                        cf.addListeners(fl);

                    cf.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }

    /**
     * Will iterate through the outboundPacketQueue and dispatch all Packets
     */
    void flushOutboundQueue()
    {
        if (channel != null && channel.isOpen())
        {
            field_181680_j.readLock().lock();

            try
            {
                while (!this.outboundPacketsQueue.isEmpty())
                {
                    NetworkManager.InboundHandlerTuplePacketListener ihtpl = (NetworkManager.InboundHandlerTuplePacketListener)this.outboundPacketsQueue.poll();
                    dispatchPacket(ihtpl.packet, ihtpl.futureListeners);
                }
            }
            finally
            {
                field_181680_j.readLock().unlock();
            }
        }
    }

    /**
     * Checks timeouts and processes all packets received
     */
    public void processReceivedPackets()
    {
        flushOutboundQueue();

        if (packetListener instanceof ITickable)
            ((ITickable)packetListener).update();

        channel.flush();
    }

    /**
     * Returns the socket address of the remote side. Server-only.
     */
    public SocketAddress getRemoteAddress()
    {
        return socketAddress;
    }

    /**
     * Closes the channel, the parameter can be used for an exit message (not certain how it gets sent)
     */
    public void closeChannel(IChatComponent message)
    {
        if (channel.isOpen())
        {
            channel.close().awaitUninterruptibly();
            terminationReason = message;
        }
    }

    /**
     * True if this NetworkManager uses a memory connection (single player game). False may imply both an active TCP
     * connection or simply no active connection at all
     */
    public boolean isLocalChannel()
    {
        return channel instanceof LocalChannel || channel instanceof LocalServerChannel;
    }

    public static NetworkManager func_181124_a(InetAddress ip, int unusedInt, boolean someFlag)
    {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        Class <? extends SocketChannel > oclass;
        LazyLoadBase <? extends EventLoopGroup > lazyloadbase;

        if (Epoll.isAvailable() && someFlag)
        {
            oclass = EpollSocketChannel.class;
            lazyloadbase = field_181125_e;
        }
        else
        {
            oclass = NioSocketChannel.class;
            lazyloadbase = CLIENT_NIO_EVENTLOOP;
        }

        new Bootstrap().group(lazyloadbase.getValue()).handler(new ChannelInitializer<Channel>()
        {
            protected void initChannel(Channel p_initChannel_1_) throws Exception
            {
                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
                }
                catch (ChannelException ce){}

                p_initChannel_1_.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"splitter",
                		(ChannelHandler)(new MessageDeserializer2())).addLast((String)"decoder", (ChannelHandler)(new MessageDeserializer(EnumPacketDirection.CLIENTBOUND))).addLast((String)"prepender",
                				(ChannelHandler)(new MessageSerializer2())).addLast((String)"encoder", (ChannelHandler)(new MessageSerializer(EnumPacketDirection.SERVERBOUND))).addLast((String)"packet_handler", (ChannelHandler)networkmanager);
            }
        }).channel(oclass).connect(ip, unusedInt).syncUninterruptibly();
        return networkmanager;
    }

    /**
     * Prepares a clientside NetworkManager: establishes a connection to the socket supplied and configures the channel
     * pipeline. Returns the newly created instance.
     */
    public static NetworkManager provideLocalClient(SocketAddress address)
    {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler(new ChannelInitializer<Channel>()
        {
            protected void initChannel(Channel c) throws Exception
            {
                c.pipeline().addLast((String)"packet_handler", (ChannelHandler)networkmanager);
            }
        })).channel(LocalChannel.class)).connect(address).syncUninterruptibly();
        return networkmanager;
    }

    /**
     * Adds an encoder+decoder to the channel pipeline. The parameter is the secret key used for encrypted communication
     */
    public void enableEncryption(SecretKey key)
    {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }

    public boolean isEncrypted()
    {
        return isEncrypted;
    }

    /**
     * Returns true if this NetworkManager has an active channel, false otherwise
     */
    public boolean isChannelOpen()
    {
        return channel != null && channel.isOpen();
    }

    public boolean hasNoChannel()
    {
        return channel == null;
    }

    /**
     * Gets the current handler for processing packets
     */
    public INetHandler getNetHandler()
    {
        return packetListener;
    }

    /**
     * If this channel is closed, returns the exit message, null otherwise.
     */
    public IChatComponent getExitMessage()
    {
        return terminationReason;
    }

    /**
     * Switches the channel to manual reading modus
     */
    public void disableAutoRead()
    {
        channel.config().setAutoRead(false);
    }

    public void setCompressionTreshold(int treshold)
    {
        if (treshold >= 0)
        {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
                ((NettyCompressionDecoder)channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            else
                channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(treshold));

            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
                ((NettyCompressionEncoder)channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            else
                this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(treshold));
        }
        else
        {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
            	channel.pipeline().remove("decompress");
            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
                channel.pipeline().remove("compress");
        }
    }

    public void checkDisconnected()
    {
        if (channel != null && !channel.isOpen())
            if (!disconnected)
            {
                disconnected = true;

                if (getExitMessage() != null)
                    getNetHandler().onDisconnect(getExitMessage());
                else if (getNetHandler() != null)
                    getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
            }
            else
                logger.warn("handleDisconnection() called twice");
    }

    static class InboundHandlerTuplePacketListener
    {
        final Packet packet;
        final GenericFutureListener <? extends Future <? super Void >> [] futureListeners;

        public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener <? extends Future <? super Void >> ... inFutureListeners)
        {
            packet = inPacket;
            futureListeners = inFutureListeners;
        }
    }
}
