package jvm.jirc.client.net.handler.impl;

import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.entity.profile.Profile;
import jvm.jirc.client.net.handler.PacketHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;

public class UpdateNameHandler extends PacketHandler{

    public UpdateNameHandler(){
        super(Opcode.UPDATE_NAME);
    }

    public void handle(final Packet pkt){
        final int id = pkt.readInt();
        final String name = pkt.readString();
        final Profile profile = ProfileManager.getInstance().get(id);
        if(profile == null)
            return;
        profile.setName(name);
    }
}
