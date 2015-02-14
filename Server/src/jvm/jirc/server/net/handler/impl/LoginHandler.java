package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.log.Log;
import jvm.jirc.server.net.handler.PacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

public class LoginHandler extends PacketHandler {

    private static final int INVALID_USER = 1;
    private static final int INVALID_PASS = 2;

    private static final int GOOD = 4;

    private static final int ERROR = 5;

    public LoginHandler(){
        super(Opcode.LOGIN);
    }

    public void handle(final ChannelHandlerContext ctx, final Packet pkt){
        final String user = pkt.readString();
        final String pass = pkt.readString();
        try{
            final Profile profile = Server.getProfileManager().get(user, false);
            if(profile == null){
                ctx.writeAndFlush(reply(INVALID_USER));
                return;
            }
            if(!profile.getPass().equals(pass)){
                profile.getLogging().push(Log.loginAttempt(profile, ctx, pass));
                ctx.writeAndFlush(reply(INVALID_PASS));
                return;
            }
            if(!profile.isInitialized() && !profile.init()){
                ctx.writeAndFlush(reply(ERROR));
                return;
            }
            ctx.writeAndFlush(reply(GOOD));
            ctx.writeAndFlush(Opcode.INIT.create(profile));
            //add friends
            profile.addConnection(ctx);
        }catch(Exception ex){
            ex.printStackTrace();
            ctx.writeAndFlush(reply(ERROR));
        }
    }
}
