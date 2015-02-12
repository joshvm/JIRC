package jvm.jirc.client.net;

import jvm.jirc.client.entity.manager.ChannelManager;
import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.entity.profile.Status;
import jvm.jirc.client.net.handler.PacketHandlerManager;
import jvm.jirc.client.net.packet.Opcode;
import jvm.jirc.client.net.packet.Packet;
import jvm.jirc.client.ui.client.ClientWindow;
import jvm.jirc.client.ui.loading.LoadingWindow;
import jvm.jirc.client.ui.registerlogin.RegisterLoginWindow;
import jvm.jirc.client.util.Utils;

import java.awt.Color;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Connection extends Thread implements Runnable{

    private static final String HOST = "localhost";
    private static final int PORT = 7495;

    private static Connection instance;

    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    private Connection() throws IOException{
        socket  = new Socket(HOST, PORT);
        socket.setSoTimeout(0);

        out = new DataOutputStream(socket.getOutputStream());
        out.flush();

        in = new DataInputStream(socket.getInputStream());
    }

    public boolean close(){
        close(in);
        close(out);
        close(socket);
        return !isConnected();
    }

    private void close(final Closeable closeable){
        try{
            closeable.close();
        }catch(Exception ex){}
    }

    private boolean write(final byte[] bytes){
        try{
            out.write(bytes);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean write(final Packet pkt){
        if(!init())
            return false;
        try{
            System.out.println("wrote: " + pkt);
            return instance.write(pkt.getPayload().array());
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean register(final String user, final String pass){
        return write(
                Opcode.REGISTER.builder()
                        .writeString(user)
                        .writeString(pass)
                        .toPacket()
        );
    }

    public static boolean login(final String user, final String pass){
        return write(
                Opcode.LOGIN.builder()
                .writeString(user)
                .writeString(pass)
                .toPacket()
        );
    }

    public static boolean updateName(final String name){
        return write(
                Opcode.UPDATE_NAME.builder()
                .writeString(name)
                .toPacket()
        );
    }

    public static boolean updateStatus(final Status status){
        return write(
                Opcode.UPDATE_STATUS.builder()
                .writeByte(status.ordinal())
                .toPacket()
        );
    }

    public boolean isConnected(){
        return socket.isConnected() && isAlive();
    }

    public void run(){
        while(isConnected()){
            try{
                final Opcode opcode = Opcode.byValue(in.readByte());
                if(opcode == null)
                    continue;
                int length = opcode.getIncomingLength();
                switch(length){
                    case -2:
                        length = in.readShort();
                        break;
                    case -1:
                        length = in.readByte();
                        break;
                }
                final byte[] bytes = new byte[length];
                in.readFully(bytes);
                final Packet pkt = new Packet(opcode, ByteBuffer.wrap(bytes));
                PacketHandlerManager.handle(pkt);
            }catch(Exception ex){
                ex.printStackTrace();
                break;
            }
        }
        if(LoadingWindow.isCreated())
            Utils.hide(LoadingWindow.getInstance());
        if(ClientWindow.isCreated())
            Utils.hide(ClientWindow.getInstance());
        ProfileManager.getInstance().clear();
        ChannelManager.getInstance().clear();
        RegisterLoginWindow.getInstance().status("You have been disconnected from the server.", Color.RED);
        RegisterLoginWindow.showNow();
    }

    public void start(){
        setPriority(MAX_PRIORITY);
        super.start();
    }

    private static boolean init(){
        if(instance == null || !instance.isConnected())
            instance = create();
       return instance != null && instance.isConnected();
    }

    private static Connection create(){
        try{
            final Connection con = new Connection();
            con.start();
            return con;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean connected(){
        return instance != null && instance.isConnected();
    }

    public static boolean disconnect(){
        return instance != null && instance.close();
    }
}
