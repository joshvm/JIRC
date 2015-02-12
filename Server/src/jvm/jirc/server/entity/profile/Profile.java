package jvm.jirc.server.entity.profile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;
import jvm.jirc.server.sql.Database;
import jvm.jirc.server.sql.profile.Profiles;
import jvm.jirc.server.sql.relationship.Relationships;
import jvm.jirc.server.util.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Profile extends Entity {

    public static final AttributeKey<Profile> KEY = AttributeKey.valueOf("profile");

    public String user;
    public String pass;
    public Rank rank;

    public String name;
    public Status status;

    private final List<ChannelHandlerContext> ctxs;
    private final Map<Integer, Channel> channels;
    private final Map<Relationship.Type, Map<Integer, Relationship>> relationships;

    public Profile(final Timestamp timestamp, final int id, final String user, final String pass, final Rank rank, final String name, final Status status){
        super(timestamp, id);
        this.user = user;
        this.pass = pass;
        this.rank = rank;
        this.name = name;
        this.status = status;

        ctxs = new ArrayList<>();

        channels = new HashMap<>();

        relationships = new HashMap<>();
    }

    public Profile(final int id, final String user, final String pass){
        this(Utils.timestamp(), id, user, pass, Rank.NONE, user, Status.ONLINE);
    }

    public Profile(){
        this(-1, null, null);
    }

    public String getUser(){
        return user;
    }

    public String getPass(){
        return pass;
    }

    public boolean setPass(final String pass, final boolean update){
        if(!update){
            this.pass = pass;
            return true;
        }
        final String oldPass = this.pass;
        this.pass = pass;
        try(final Profiles profiles = Database.profiles()){
            profiles.updatePass(this);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            this.pass = oldPass;
            return false;
        }
    }

    public Rank getRank(){
        return rank;
    }

    public boolean setRank(final Rank rank, final boolean update){
        if(!update){
            this.rank = rank;
            return true;
        }
        final Rank oldRank = this.rank;
        this.rank = rank;
        try(final Profiles profiles = Database.profiles()){
            profiles.updateRank(this);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            this.rank = oldRank;
            return false;
        }
    }

    public String getName(){
        return name;
    }

    public boolean setName(final String name, final boolean update){
        if(!update){
            this.name = name;
            return true;
        }
        final String oldName = this.name;
        this.name = name;
        try(final Profiles profiles = Database.profiles()){
            profiles.updateName(this);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            this.name = oldName;
            return false;
        }
    }

    public Status getStatus(){
        return status;
    }

    public boolean setStatus(final Status status, final boolean update){
        if(!update){
            this.status = status;
            return true;
        }
        final Status oldStatus = this.status;
        this.status = status;
        try(final Profiles profiles = Database.profiles()){
            profiles.updateStatus(this);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            this.status = oldStatus;
            return false;
        }
    }

    public boolean save(){
        try(final Profiles profiles = Database.profiles()){
            profiles.update(this);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Packet toPacket(final Opcode opcode){
        return opcode.builder().writeProfile(this).toPacket();
    }

    public void addConnection(final ChannelHandlerContext ctx){
        ctx.attr(KEY).set(this);
        ctxs.add(ctx);
    }

    public void removeConnection(final ChannelHandlerContext ctx){
        ctxs.remove(ctx);
    }

    public boolean isConnected(){
        return !ctxs.isEmpty();
    }

    public ChannelHandlerContext getLastConnection(){
        return ctxs.isEmpty() ? null : ctxs.get(ctxs.size()-1);
    }

    public void send(final Packet pkt){
        ctxs.forEach(ctx -> ctx.writeAndFlush(pkt));
    }

    public boolean inChannel(final Channel channel){
        return channels.containsKey(channel.getId());
    }

    public void addChannel(final Channel channel){
        channels.put(channel.getId(), channel);
    }

    public void removeChannel(final Channel channel){
        channels.remove(channel.getId());
    }

    public Collection<Channel> getChannels(){
        return channels.values();
    }

    public boolean addRelationship(final Relationship.Type type, final int other, final boolean update){
        final Relationship relationship = new Relationship(type, id, other);
        addRelationship(relationship);
        if(!update)
            return true;
        try(final Relationships relationships = Database.relationships()){
            relationships.insert(relationship);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            removeRelationship(relationship);
            return false;
        }
    }

    public boolean removeRelationship(final Relationship.Type type, final int other, final boolean update){
        final Relationship relationship = relationships.get(type).get(other);
        if(relationship == null)
            return true;
        removeRelationship(relationship);
        if(!update)
            return true;
        try(final Relationships relationships = Database.relationships()){
            relationships.delete(relationship);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            addRelationship(relationship);
            return false;
        }
    }

    public Map<Integer, Profile> getFriends(){
        if(!relationships.containsKey(Relationship.Type.FRIEND))
            return new HashMap<>();
        return relationships.get(Relationship.Type.FRIEND).values().stream().collect(
                Collectors.toMap(Relationship::getToId, Relationship::getTo)
        );
    }

    public Set<Profile> getInteractingProfiles(){
        final Set<Profile> profiles = new HashSet<>();
        profiles.addAll(getFriends().values());
        getChannels().forEach(c -> profiles.addAll(c.getProfiles().values()));
        return profiles;
    }

    private void addRelationship(final Relationship relationship){
        if(!relationships.containsKey(relationship.getType()))
            relationships.put(relationship.getType(), new HashMap<>());
        relationships.get(relationship.getType()).put(relationship.getToId(), relationship);
    }

    private void removeRelationship(final Relationship relationship){
        relationships.get(relationship.getType()).remove(relationship.getToId());
    }

    public void load() throws Exception{
        Database.demandRelationships().get(id).forEach(this::addRelationship);
    }

    public void dispose(){
        channels.clear();
        user = pass = name = null;
        rank = null;
        status = null;
        System.gc();
    }

    public String toString(){
        return String.format("Profile[id=%s,user=%s,pass=%s,rank=%s,name=%s,status=%s]", id, user, pass, rank, name, status);
    }
}
