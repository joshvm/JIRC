package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Relationship;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class AddFriendHandler extends ProfilePacketHandler {

    public AddFriendHandler(){
        super(Opcode.ADD_FRIEND);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final int id = pkt.readInt();
        final Profile to = Server.getProfileManager().get(id, false);
        if(to == null){
            profile.send(Opcode.SERVER_MESSAGE.create("Unable to find player"));
            return;
        }
        if(!profile.addRelationship(Relationship.Type.FRIEND, to.getId(), true)){
            profile.send(Opcode.SERVER_MESSAGE.create("Unable to add player"));
            return;
        }
        profile.send(reply(to));
    }
}
