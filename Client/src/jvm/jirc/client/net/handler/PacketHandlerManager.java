package jvm.jirc.client.net.handler;

import jvm.jirc.client.net.handler.impl.InitHandler;
import jvm.jirc.client.net.handler.impl.LoginHandler;
import jvm.jirc.client.net.handler.impl.RegisterHandler;
import jvm.jirc.client.net.handler.impl.UpdateNameHandler;
import jvm.jirc.client.net.handler.impl.UpdateRankHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public final class PacketHandlerManager {

    private static final Map<Opcode, PacketHandler> MAP = new HashMap<>();

    static{
        addHandler(new RegisterHandler());
        addHandler(new LoginHandler());
        addHandler(new InitHandler());
        addHandler(new UpdateRankHandler());
        addHandler(new UpdateNameHandler());
    }

    private PacketHandlerManager(){}

    private static void addHandler(final PacketHandler handler){
        MAP.put(handler.getOpcode(), handler);
    }

    public static void handle(final Packet pkt){
        System.out.println("received: " + pkt);
        try{
            MAP.get(pkt.getOpcode()).handle(pkt);
        }finally{
            pkt.getPayload().clear();
        }
    }
}
