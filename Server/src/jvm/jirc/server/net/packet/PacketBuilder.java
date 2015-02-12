package jvm.jirc.server.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.entity.profile.Profile;

public class PacketBuilder {

    private final Opcode opcode;
    private final ByteBuf buf;

    public PacketBuilder(final Opcode opcode){
        this.opcode = opcode;

        buf = Unpooled.buffer();
    }

    public PacketBuilder writeBoolean(final boolean b){
        buf.writeBoolean(b);
        return this;
    }

    public PacketBuilder writeByte(final int b){
        buf.writeByte(b);
        return this;
    }

    public PacketBuilder writeChar(final char c){
        buf.writeChar(c);
        return this;
    }

    public PacketBuilder writeShort(final int s){
        buf.writeShort(s);
        return this;
    }

    public PacketBuilder writeInt(final int i){
        buf.writeInt(i);
        return this;
    }

    public PacketBuilder writeFloat(final float f){
        buf.writeFloat(f);
        return this;
    }

    public PacketBuilder writeLong(final long l){
        buf.writeLong(l);
        return this;
    }

    public PacketBuilder writeDouble(final double d){
        buf.writeDouble(d);
        return this;
    }

    public PacketBuilder writeString(final String str){
        for(final char c : str.toCharArray())
            writeByte((byte)c);
        return writeByte(0);
    }

    public PacketBuilder writeProfile(final Profile profile){
        return writeInt(profile.getId())
                .writeString(profile.getUser())
                .writeByte(profile.getRank().ordinal())
                .writeString(profile.getName())
                .writeByte(profile.getStatus().ordinal());
    }

    public PacketBuilder writeChannel(final Channel channel){
        return writeInt(channel.getId())
                .writeInt(channel.getOwnerId())
                .writeString(channel.getName())
                .writeString(channel.getDescription());
    }

    public Packet toPacket(){
        final int readableLength = buf.readableBytes();
        final ByteBuf payload = Unpooled.buffer(1 +  readableLength + (opcode.getOutgoingLength() < 0 ? Math.abs(opcode.getOutgoingLength()) : 0));
        payload.writeByte(opcode.getValue());
        switch(opcode.getOutgoingLength()){
            case -2:
                payload.writeShort(readableLength);
                break;
            case -1:
                payload.writeByte(readableLength);
                break;
        }
        payload.writeBytes(buf, 0, readableLength);
        return new Packet(opcode, payload);
    }
}
