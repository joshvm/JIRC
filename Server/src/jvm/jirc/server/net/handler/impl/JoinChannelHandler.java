package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class JoinChannelHandler extends ProfilePacketHandler{

    public JoinChannelHandler(){
        super(Opcode.JOIN_CHANNEL);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final int id = pkt.readInt();
        final Channel channel = Server.getChannelManager().get(id, true);
        if(channel == null){
            profile.send(Opcode.SERVER_MESSAGE.create("Error finding channel"));
            return;
        }
        
    }
}
