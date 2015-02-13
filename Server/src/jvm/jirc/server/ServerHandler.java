package jvm.jirc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.net.handler.PacketHandler;
import jvm.jirc.server.net.handler.impl.*;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends ChannelInboundHandlerAdapter{

    private static final Map<Opcode, PacketHandler> HANDLERS = new HashMap<>();

    static{
        addHandler(new RegisterHandler());
        addHandler(new LoginHandler());
        addHandler(new MessageHandler());
        addHandler(new ProfileInitHandler());
        addHandler(new UpdateNameHandler());
        addHandler(new UpdateStatusHandler());
    }

    public void handlerRemoved(final ChannelHandlerContext ctx){
        final Profile profile = ctx.attr(Profile.KEY).getAndRemove();
        if(profile == null)
            return;
        profile.removeConnection(ctx);
        if(profile.isConnected())
            return;
        profile.getChannels().forEach(
                c -> c.leave(profile)
        );
        Server.getProfileManager().remove(profile);
        profile.dispose();
    }

    public void channelRead(final ChannelHandlerContext ctx, final Object msg){
        final Packet pkt = (Packet)msg;
        System.out.println("received packet: " + pkt);
        final PacketHandler handler = HANDLERS.get(pkt.getOpcode());
        try{
            if(handler != null)
                handler.handle(ctx, pkt);
        }finally{
            pkt.release();
        }
    }

    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable err){
        err.printStackTrace();
        ctx.close();
    }

    private static void addHandler(final PacketHandler handler){
        HANDLERS.put(handler.getOpcode(), handler);
    }
}
