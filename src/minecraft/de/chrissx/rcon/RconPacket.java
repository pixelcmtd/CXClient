package de.chrissx.rcon;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.CharsetUtil;

public class RconPacket {

	int length;
	int id;
	int type;
	String payload;

	public RconPacket(int length, int id, int type, String payload) {
		this.length = length;
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
		return new RconPacket(length, id, type, payload);
	}

	public ByteBuf encode() {
		ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.buffer(length + 14).order(ByteOrder.LITTLE_ENDIAN);
		buf.writeInt(length);
		buf.writeInt(id);
		buf.writeInt(type);
		buf.writeBytes(Unpooled.copiedBuffer(payload, CharsetUtil.UTF_8));
		buf.writeByte(0);
		buf.writeByte(0);
		return buf;
	}

	public static RconPacket response(int id, String payload) {
		return new RconPacket(payload.length() + 10, id, 0, payload);
	}

}
