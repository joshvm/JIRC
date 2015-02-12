package jvm.jirc.client.net.handler.impl;

import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.entity.profile.Profile;
import jvm.jirc.client.net.handler.PacketHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;

public class ProfileInitHandler extends PacketHandler{

    public ProfileInitHandler(){
        super(Opcode.PROFILE_INIT);
    }

    public void handle(final Packet pkt){
        final Profile profile = Profile.parse(pkt);
        ProfileManager.getInstance().add(profile);
    }
}
