package jvm.jirc.client.net.handler.impl;

import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.entity.profile.Profile;
import jvm.jirc.client.entity.profile.Status;
import jvm.jirc.client.net.handler.PacketHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;

public class UpdateStatusHandler extends PacketHandler {

    public UpdateStatusHandler(){
        super(Opcode.UPDATE_STATUS);
    }

    public void handle(final Packet pkt){
        final int id = pkt.readInt();
        final Status status = Status.values()[pkt.readByte()];
        final Profile profile = ProfileManager.getInstance().get(id);
        if(profile == null)
            return;
        profile.setStatus(status);
    }
}
