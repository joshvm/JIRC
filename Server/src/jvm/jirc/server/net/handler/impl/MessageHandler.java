package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.log.Log;
import jvm.jirc.server.net.handler.ProfilePacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class MessageHandler extends ProfilePacketHandler{

    public MessageHandler(){
        super(Opcode.MESSAGE);
    }

    public void handle(final ChannelHandlerContext ctx, final Profile profile, final Packet pkt){
        final int toId = pkt.readInt();
        final String msg = pkt.readString();
        final Profile to = Server.getProfileManager().get(toId, false);
        if(to == null){
            profile.sendServerMessage("Error finding player: %d", toId);
            return;
        }
        if(!to.isConnected()){
            profile.sendServerMessage("Player not online: %s", to.getUser());
            return;
        }
        if(to.isBlocked(profile)){
            profile.sendServerMessage("%s has blocked you", to.getUser());
            return;
        }
        profile.getLogging().push(Log.message(profile, to, msg));
        final Packet out = Opcode.MESSAGE.create(profile, to, msg);
        profile.send(out);
        to.send(out);
    }
}
