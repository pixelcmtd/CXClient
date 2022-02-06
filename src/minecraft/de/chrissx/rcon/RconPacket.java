package de.chrissx.rcon;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.CharsetUtil;

public class RconPacket {

	int id;
	int type;
	String payload;

	public RconPacket(int id, int type, String payload) {
		this.id = id;
		this.type = type;
		this.payload = payload;
	}

	public static RconPacket parse(ByteBuf buf) {
		buf = buf.order(ByteOrder.LITTLE_ENDIAN);
		int length = buf.readInt();
		int id = buf.readInt();
		int type = buf.readInt();
		String payload = buf.readBytes(length - 10).toString(CharsetUtil.UTF_8);
		assert(buf.readByte() == 0);
		assert(buf.readByte() == 0);
		return new RconPacket(id, type, payload);
	}

	// TODO: do partitioning here
	public ByteBuf encode() {
		ByteBuf payloadBytes = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
		ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.buffer(payloadBytes.array().length + 14).order(ByteOrder.LITTLE_ENDIAN);
		buf.writeInt(payloadBytes.array().length + 10);
		buf.writeInt(id);
		buf.writeInt(type);
		buf.writeBytes(Unpooled.copiedBuffer(payload, CharsetUtil.UTF_8));
		buf.writeByte(0);
		buf.writeByte(0);
		return buf;
	}

	@Override
	public String toString() {
		return "{id=" + id + " type=" + type + " payload='" + payload + "'}";
	}

}
