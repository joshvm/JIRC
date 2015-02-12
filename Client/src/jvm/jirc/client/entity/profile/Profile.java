package jvm.jirc.client.entity.profile;

import jvm.jirc.client.entity.Entity;
import jvm.jirc.client.net.packet.Packet;

public class Profile extends Entity {

    public static final int RANK_FLAG = 1 << 1;
    public static final int NAME_FLAG = 1 << 2;
    public static final int STATUS_FLAG = 1 << 3;

    private final String user;
    private Rank rank;
    private String name;
    private Status status;

    public Profile(final int id, final String user, final Rank rank, final String name, final Status status){
        super(id);
        this.user = user;
        this.rank = rank;
        this.name = name;
        this.status = status;
    }

    public String getUser(){
        return user;
    }

    public Rank getRank(){
        return rank;
    }

    public void setRank(final Rank rank){
        this.rank = rank;
        fireOnUpdate(RANK_FLAG);
    }

    public String getName(){
        return name;
    }

    public void setName(final String name){
        this.name = name;
        fireOnUpdate(NAME_FLAG);
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(final Status status){
        this.status = status;
        fireOnUpdate(STATUS_FLAG);
    }

    public void dispose(){
        rank = null;
        name = null;
        status = null;
        super.dispose();
    }

    public static Profile parse(final Packet pkt){
        return new Profile(
                pkt.readInt(),
                pkt.readString(),
                Rank.values()[pkt.readByte()],
                pkt.readString(),
                Status.values()[pkt.readByte()]
        );
    }
}
