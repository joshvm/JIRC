package jvm.jirc.client.net.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Packet {

    private final Opcode opcode;
    private final ByteBuffer buffer;
    private final int length;

    public Packet(final Opcode opcode, final ByteBuffer buffer){
        this.opcode = opcode;

        this.buffer = buffer;

        length = buffer.capacity();
    }

    public Opcode getOpcode(){
        return opcode;
    }

    public int getLength(){
        return length;
    }

    public ByteBuffer getPayload(){
        return buffer;
    }

    public byte readByte(){
        return buffer.get();
    }

    public short readShort(){
        return buffer.getShort();
    }

    public int readInt(){
        return buffer.getInt();
    }

    public String readString(){
        final StringBuilder bldr = new StringBuilder();
        byte b;
        while((b = readByte()) != 0)
            bldr.append((char)b);
        return bldr.toString();
    }

    public String toString(){
        return String.format("Packet[opcode=%s,buf=%s,data=%s,length=%s]", opcode, buffer, Arrays.toString(buffer.array()), length);
    }

}
