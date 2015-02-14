package jvm.jirc.server.log;

import io.netty.channel.ChannelHandlerContext;
import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.entity.profile.Relationship;
import jvm.jirc.server.entity.profile.Status;
import jvm.jirc.server.util.Utils;

import java.sql.Timestamp;

public class Log {

    public enum Type {
        LOGIN,
        LOGOUT,
        LOGIN_ATTEMPT,
        CHANGE_PASS,
        CHANGE_RANK,
        CHANGE_NAME,
        CHANGE_STATUS,
        JOIN_CHANNEL,
        LEAVE_CHANNEL,
        ADD_RELATIONSHIP,
        REMOVE_RELATIONSHIP,
        MESSAGE,
        CHANNEL_MESSAGE,
        CHANGE_DESCRIPTION,
        CHANGE_CHANNEL_RANK,
    }

    private final Timestamp timestamp;
    private final Type type;
    private final int entityId;
    private final String info;

    public Log(final Timestamp timestamp, final Type type, final int entityId, final String info){
        this.timestamp = timestamp;
        this.type = type;
        this.entityId = entityId;
        this.info = info;
    }

    private Log(final Type type, final Entity entity, final String fmt, final Object... args){
        this(Utils.timestamp(), type, entity.getId(), String.format(fmt, args));
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public Type getType(){
        return type;
    }

    public int getEntityId(){
        return entityId;
    }

    public String getInfo(){
        return info;
    }

    public static Log login(final Profile profile, final ChannelHandlerContext ctx){
        return new Log(Type.LOGIN, profile, Utils.ip(ctx));
    }

    public static Log logout(final Profile profile, final ChannelHandlerContext ctx){
        return new Log(Type.LOGOUT, profile, Utils.ip(ctx));
    }

    public static Log loginAttempt(final Profile profile, final ChannelHandlerContext ctx, final String pass){
        return new Log(Type.LOGIN_ATTEMPT, profile, "%s: %s", Utils.ip(ctx), pass);
    }

    private static Log changeLog(final Type type, final Entity entity, final Object oldValue, final Object newValue){
        return new Log(type, entity, "%s -> %s", oldValue, newValue);
    }

    public static Log changePass(final Profile profile, final String oldPass, final String newPass){
        return changeLog(Type.CHANGE_PASS, profile, oldPass, newPass);
    }

    public static Log changeRank(final Profile profile, final Rank oldRank, final Rank newRank){
        return changeLog(Type.CHANGE_RANK, profile, oldRank, newRank);
    }

    public static Log changeName(final Profile profile, final String oldName, final String newName){
        return changeLog(Type.CHANGE_NAME, profile, oldName, newName);
    }

    public static Log changeStatus(final Profile profile, final Status oldStatus, final Status newStatus){
        return changeLog(Type.CHANGE_STATUS, profile, oldStatus, newStatus);
    }

    public static Log joinChannel(final Profile profile, final Channel channel){
        return new Log(Type.JOIN_CHANNEL, profile, "%s (ID: %d)", channel.getName(), channel.getId());
    }

    public static Log joinChannel(final Channel channel, final Profile profile){
        return new Log(Type.JOIN_CHANNEL, channel, "%s (ID: %d)", profile.getUser(), profile.getId());
    }

    public static Log leaveChannel(final Profile profile, final Channel channel){
        return new Log(Type.LEAVE_CHANNEL, profile, "%s (ID: %d)", channel.getName(), channel.getId());
    }

    public static Log leaveChannel(final Channel channel, final Profile profile){
        return new Log(Type.LEAVE_CHANNEL, channel, "%s (ID: %d)", profile.getUser(), profile.getId());
    }

    public static Log addRelationship(final Relationship relationship){
        return new Log(Type.ADD_RELATIONSHIP, relationship.getProfile(), "%s - %s (ID: %d)", relationship.getType(), relationship.getTarget().getUser(), relationship.getTargetId());
    }

    public static Log removeRelationship(final Relationship relationship){
        return new Log(Type.REMOVE_RELATIONSHIP, relationship.getProfile(), "%s - %s (ID: %d)", relationship.getType(), relationship.getTarget().getUser(), relationship.getTargetId());
    }

    public static Log message(final Channel channel, final Profile profile, final String msg){
        return new Log(Type.MESSAGE, channel, "%s (ID: %d) - %s", profile.getUser(), profile.getId(), msg);
    }

    public static Log channelMessage(final Profile profile, final Channel channel, final String msg){
        return new Log(Type.CHANNEL_MESSAGE, profile, "%s (ID: %d) - %s", channel.getName(), channel.getId(), msg);
    }

    public static Log message(final Profile profile, final Profile other, final String msg){
        return new Log(Type.MESSAGE, profile, "%s (ID: %d) - %s", other.getUser(), other.getId(), msg);
    }

    public static Log changeChannelDescription(final Channel channel, final String oldDescription, final String newDescription){
        return changeLog(Type.CHANGE_DESCRIPTION, channel, oldDescription, newDescription);
    }

    public static Log changeChannelRank(final Channel channel, final Profile profile, final Rank oldRank, final Rank newRank){
        return new Log(Type.CHANGE_CHANNEL_RANK, channel, "%s (ID: %d) %s -> %s", profile.getUser(), profile.getId(), oldRank, newRank);
    }
}
