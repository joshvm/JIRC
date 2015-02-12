package jvm.jirc.server.entity.profile;

import jvm.jirc.server.Server;

public class Relationship {

    public enum Type{
        FRIEND
    }

    private Type type;
    private int from;
    private int to;

    public Relationship(final Type type, final int from, final int to){
        this.type = type;
        this.from = from;
        this.to = to;
    }

    public Relationship(){
        this(null, -1, -1);
    }

    public Type getType(){
        return type;
    }

    public int getFromId(){
        return from;
    }

    public Profile getFrom(){
        return Server.getProfileManager().get(from, true);
    }

    public int getToId(){
        return to;
    }

    public Profile getTo(){
        return Server.getProfileManager().get(to, true);
    }

    public static Relationship friend(final int from, final int to){
        return new Relationship(Type.FRIEND, from, to);
    }
}
