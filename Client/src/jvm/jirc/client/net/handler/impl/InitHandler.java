package jvm.jirc.client.net.handler.impl;

import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.entity.profile.MyProfile;
import jvm.jirc.client.net.handler.PacketHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;
import jvm.jirc.client.ui.loading.LoadingWindow;

public class InitHandler extends PacketHandler{

    public InitHandler(){
        super(Opcode.INIT);
    }

    public void handle(final Packet pkt){
        ProfileManager.getInstance().setMyProfile(MyProfile.parse(pkt));
        LoadingWindow.ready();
    }
}
