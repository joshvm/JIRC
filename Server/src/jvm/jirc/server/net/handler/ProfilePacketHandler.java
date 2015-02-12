package jvm.jirc.server.net.handler;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public abstract class ProfilePacketHandler extends PacketHandler{

    protected ProfilePacketHandler(final Opcode opcode){
        super(opcode);
    }

    public void handle(final ChannelHandlerContext ctx, final Packet pkt){
        final Profile profile = ctx.attr(Profile.KEY).get();
        if(profile != null)
            handle(ctx, profile, pkt);
    }

    public abstract void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt);
}
