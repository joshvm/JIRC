package jvm.jirc.client.net.handler.impl;

import jvm.jirc.client.net.handler.PacketHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;
import jvm.jirc.client.ui.registerlogin.RegisterLoginWindow;

public class LoginHandler extends PacketHandler{

    public LoginHandler(){
        super(Opcode.LOGIN);
    }

    public void handle(final Packet pkt){
        RegisterLoginWindow.getInstance().handleLoginResponse(pkt.readByte());
    }
}
