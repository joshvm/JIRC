package jvm.jirc.server.log;

import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.entity.profile.Status;
import jvm.jirc.server.util.Utils;

import java.sql.Timestamp;

public class Log {

    public enum Type {
        CHANGE_PASS,
        CHANGE_RANK,
        CHANGE_NAME,
        CHANGE_STATUS,
        JOIN_CHANNEL,
        LEAVE_CHANNEL,
        ADD_RELATIONSHIP,
        REMOVE_RELATIONSHIP,
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

    public static Log leaveChannel(final Profile profile, final Channel channel){
        return new Log(Type.LEAVE_CHANNEL, profile, "%s (ID: %d)", channel.getName(), channel.getId());
    }
}
