package jvm.jirc.client.net.handler.impl;

import jvm.jirc.client.net.handler.PacketHandler;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;
import jvm.jirc.client.ui.registerlogin.RegisterLoginWindow;

public class RegisterHandler extends PacketHandler {

    public RegisterHandler(){
        super(Opcode.REGISTER);
    }

    public void handle(final Packet pkt){
        RegisterLoginWindow.getInstance().handleRegisterResponse(pkt.readByte());
    }
}
