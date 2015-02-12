package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class UpdateNameHandler extends ProfilePacketHandler{

    public UpdateNameHandler(){
        super(Opcode.UPDATE_NAME);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final String name = pkt.readString().trim();
        if(name.length() < 1 || name.length() > 50){
            ctx.writeAndFlush(Opcode.SERVER_MESSAGE.create("Name must be between 1 and 50 characters"));
            return;
        }
        if(!profile.setName(name, true)){
            profile.send(Opcode.SERVER_MESSAGE.create("Error changing your name"));
            return;
        }
        final Packet out = reply(profile);
        profile.send(out);
        profile.getInteractingProfiles().forEach(p -> p.send(out));
    }
}
