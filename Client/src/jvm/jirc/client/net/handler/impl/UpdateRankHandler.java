package jvm.jirc.client.net.handler.impl;

import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.entity.profile.Profile;
import jvm.jirc.client.entity.profile.Rank;
import jvm.jirc.client.net.handler.PacketHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;

public class UpdateRankHandler extends PacketHandler{

    public UpdateRankHandler(){
        super(Opcode.UPDATE_RANK);
    }

    public void handle(final Packet pkt){
        final int id = pkt.readInt();
        final Rank rank = Rank.values()[pkt.readByte()];
        final Profile profile = ProfileManager.getInstance().get(id);
        if(profile == null)
            return;
        profile.setRank(rank);
    }
}
