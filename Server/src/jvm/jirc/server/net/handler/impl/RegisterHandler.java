package jvm.jirc.server.net.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.Server;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.entity.profile.Status;
import jvm.jirc.server.net.handler.PacketHandler;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;
import jvm.jirc.server.sql.Database;
import jvm.jirc.server.sql.profile.Profiles;

public class RegisterHandler extends PacketHandler{

    private static final String USER_EXCEPTIONS = "_";
    private static final String PASS_EXCEPTIONS = "_.!@#$%^&*-+";

    private static final int MIN_USER_LENGTH = 1;
    private static final int MAX_USER_LENGTH = 20;

    private static final int MIN_PASS_LENGTH = 5;
    private static final int MAX_PASS_LENGTH = 50;

    private static final int INVALID_USER = 1;
    private static final int INVALID_PASS = 2;
    private static final int USER_TAKEN = 3;

    private static final int GOOD = 4;
    private static final int ERROR = 5;

    public RegisterHandler(){
        super(Opcode.REGISTER);
    }

    public void handle(final ChannelHandlerContext ctx, final Packet pkt){
        final String user = pkt.readString();
        final String pass = pkt.readString();
        if(!isValidUser(user)){
            ctx.writeAndFlush(reply(INVALID_USER));
            return;
        }
        if(!isValidPass(pass)){
            ctx.writeAndFlush(reply(INVALID_PASS));
            return;
        }
        if(Server.getProfileManager().get(user, false) != null){
            ctx.writeAndFlush(reply(USER_TAKEN));
            return;
        }
        try(final Profiles profiles = Database.profiles()){
            profiles.insert(user, pass, Rank.NONE, user, Status.ONLINE);
            ctx.writeAndFlush(reply(GOOD));
        }catch(Exception ex){
            ex.printStackTrace();
            ctx.writeAndFlush(reply(ERROR));
        }
    }

    private static boolean isValidUser(final String user){
        return isValidLength(user.length(), MIN_USER_LENGTH, MAX_USER_LENGTH) && isValidChars(user, USER_EXCEPTIONS);
    }

    private static boolean isValidPass(final String pass){
        return isValidLength(pass.length(), MIN_PASS_LENGTH, MAX_PASS_LENGTH) && isValidChars(pass, PASS_EXCEPTIONS);
    }

    private static boolean isValidChars(final String str, final String exceptions){
        for(final char c : str.toCharArray())
            if(!Character.isLetterOrDigit(c) && exceptions.indexOf(c) < 0)
                return false;
        return true;
    }

    private static boolean isValidLength(final int length, final int minLength, final int maxLength){
        return length >= minLength && length <= maxLength;
    }
}
