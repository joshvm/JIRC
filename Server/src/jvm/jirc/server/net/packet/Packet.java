package jvm.jirc.server.net.packet;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

public class Packet {

    private final Opcode opcode;
    private final ByteBuf buf;
    private final int length;

    public Packet(final Opcode opcode, final ByteBuf buf){
        this.opcode = opcode;
        this.buf = buf;

        length = buf.readableBytes();
    }

    public Opcode getOpcode(){
        return opcode;
    }

    public int getLength(){
        return length;
    }

    public ByteBuf getPayload(){
        return buf;
    }

    public byte readByte(){
        return buf.readByte();
    }

    public short readShort(){
        return buf.readShort();
    }

    public int readInt(){
        return buf.readInt();
    }

    public String readString(){
        final StringBuilder bldr = new StringBuilder();
        byte b;
        while((b = readByte()) != 0)
             bldr.append((char)b);
        return bldr.toString();
    }

    public void release(){
        buf.release();
    }

    public String toString(){
        return String.format("Packet[opcode=%s,buf=%s,data=%s,length=%s]", opcode, buf, Arrays.toString(buf.array()), length);
    }
}
