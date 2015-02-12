package jvm.jirc.server.net.handler;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public abstract class PacketHandler {

    private final Opcode opcode;

    protected PacketHandler(final Opcode opcode){
        this.opcode = opcode;
    }

    public Opcode getOpcode(){
        return opcode;
    }
    
    public Packet reply(final Object... args){
        return opcode.create(args);
    }

    public abstract void handle(final ChannelHandlerContext ctx, final Packet pkt);

}
