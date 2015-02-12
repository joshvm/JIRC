package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Status;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class UpdateStatusHandler extends ProfilePacketHandler {

    public UpdateStatusHandler(){
        super(Opcode.UPDATE_STATUS);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final int statusIdx = pkt.readByte();
        final Status status = Status.forIndex(statusIdx);
        if(status == null){
            ctx.writeAndFlush(Opcode.SERVER_MESSAGE.create("Invalid status"));
            return;
        }
        if(!profile.setStatus(status, true)){
            profile.send(Opcode.SERVER_MESSAGE.create("Error changing status"));
            return;
        }
        final Packet out = reply(profile);
        profile.send(out);
        profile.getFriends().values().forEach(f -> f.send(out));
        profile.getChannels().forEach(c -> c.sendExcept(out, profile));
    }
}
