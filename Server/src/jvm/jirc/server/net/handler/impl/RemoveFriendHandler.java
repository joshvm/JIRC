package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Relationship;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class RemoveFriendHandler extends ProfilePacketHandler{

    public RemoveFriendHandler(){
        super(Opcode.REMOVE_FRIEND);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final int id = pkt.readInt();
        final Profile other = Server.getProfileManager().get(id, false);
        if(!profile.getFriends().containsKey(id)){
            ctx.writeAndFlush(Opcode.SERVER_MESSAGE.create("Not your friend: " + other.getName()));
            return;
        }
        if(!profile.removeRelationship(Relationship.Type.FRIEND, other.getId(), true)){
            ctx.writeAndFlush(Opcode.SERVER_MESSAGE.create("Unable to remove friend: " + other.getName()));
            return;
        }
        ctx.writeAndFlush(reply(other));
    }
}
