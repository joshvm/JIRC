package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class LeaveChannelHandler extends ProfilePacketHandler{

    public LeaveChannelHandler(){
        super(Opcode.LEAVE_CHANNEL);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final int id = pkt.readInt();
        final Channel channel = Server.getChannelManager().get(id, false);
        if(channel == null){
            profile.sendServerMessage("Channel does not exist: %d", id);
            return;
        }
        if(!channel.contains(profile)){
            profile.sendServerMessage("You're not in this channel!");
            return;
        }
        channel.leave(profile);
    }
}
