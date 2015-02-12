package jvm.jirc.client.net.handler;

import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;

public abstract class PacketHandler {

    private final Opcode opcode;

    protected PacketHandler(final Opcode opcode){
        this.opcode = opcode;
    }

    public Opcode getOpcode(){
        return opcode;
    }

    public abstract void handle(final Packet pkt);

}
