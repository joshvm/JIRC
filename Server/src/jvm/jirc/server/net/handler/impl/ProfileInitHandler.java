package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class ProfileInitHandler extends ProfilePacketHandler{

    public ProfileInitHandler(){
        super(Opcode.PROFILE_INIT);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final int id = pkt.readInt();
        final Profile target = Server.getProfileManager().get(id, false);
        if(target == null)
            return;
        profile.send(reply(target));
    }
}
