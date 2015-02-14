package jvm.jirc.server.entity.profile;

import jvm.jirc.server.Server;
import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.util.Utils;

import java.sql.Timestamp;

public class Relationship extends Entity {

    public enum Type{
        FRIEND, BLOCKED
    }

    private final int targetId;
    private final Type type;

    public Relationship(final Timestamp timestamp, final int id, final int targetId, final Type type){
        super(timestamp, id);
        this.targetId = targetId;
        this.type = type;
    }

    public Relationship(final Profile profile, final Profile target, final Type type){
        this(Utils.timestamp(), profile.getId(), target.getId(), type);
    }

    public Profile getProfile(){
        return Server.getProfileManager().get(id, false);
    }

    public int getTargetId(){
        return targetId;
    }

    public Profile getTarget(){
        return Server.getProfileManager().get(targetId, false);
    }

    public Type getType(){
        return type;
    }

    protected void load(){}

}
