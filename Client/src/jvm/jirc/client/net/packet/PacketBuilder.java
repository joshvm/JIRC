package jvm.jirc.client.net.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

public final class PacketBuilder {

    private final Opcode opcode;
    private final ByteArrayOutputStream baos;
    private final DataOutputStream out;

    public PacketBuilder(final Opcode opcode){
        this.opcode = opcode;

        out = new DataOutputStream(baos = new ByteArrayOutputStream());
    }

    public PacketBuilder writeByte(final int b){
        try{
            out.writeByte(b);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public PacketBuilder writeShort(final int s){
        try{
            out.writeShort(s);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public PacketBuilder writeInt(final int i){
        try{
            out.writeInt(i);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public PacketBuilder writeString(final String s){
        for(final char c : s.toCharArray())
            writeByte((byte)c);
        return writeByte(0);
    }

    public Packet toPacket(){
        final byte[] bytes = baos.toByteArray();
        final ByteBuffer out = ByteBuffer.allocate(1 + bytes.length + (opcode.getOutgoingLength() < 0 ? Math.abs(opcode.getOutgoingLength()) : 0));
        out.put((byte)opcode.get());
        switch(opcode.getOutgoingLength()){
            case -2:
                out.putShort((short)bytes.length);
                break;
            case -1:
                out.put((byte)bytes.length);
                break;
        }
        out.put(bytes);
        return new Packet(opcode, out);
    }
}
