package jvm.jirc.server.entity.profile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.log.Log;
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

    private final Map<Integer, Relationship> relationships;

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
        final String oldPass = this.pass;
        this.pass = pass;
        if(!update){
            logging.push(Log.changePass(this, oldPass, pass));
            return true;
        }
        try(final Profiles profiles = Database.profiles()){
            profiles.updatePass(this);
            logging.push(Log.changePass(this, oldPass, pass));
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
        final Rank oldRank = this.rank;
        this.rank = rank;
        if(!update){
            logging.push(Log.changeRank(this, oldRank, rank));
            return true;
        }
        try(final Profiles profiles = Database.profiles()){
            profiles.updateRank(this);
            logging.push(Log.changeRank(this, oldRank, rank));
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
        final String oldName = this.name;
        this.name = name;
        if(!update){
            logging.push(Log.changeName(this, oldName, name));
            return true;
        }
        try(final Profiles profiles = Database.profiles()){
            profiles.updateName(this);
            logging.push(Log.changeName(this, oldName, name));
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
        final Status oldStatus = this.status;
        this.status = status;
        if(!update){
            logging.push(Log.changeStatus(this, oldStatus, status));
            return true;
        }
        try(final Profiles profiles = Database.profiles()){
            profiles.updateStatus(this);
            logging.push(Log.changeStatus(this, oldStatus, status));
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
        logging.push(Log.login(this, ctx));
    }

    public void removeConnection(final ChannelHandlerContext ctx){
        ctxs.remove(ctx);
        logging.push(Log.logout(this, ctx));
        if(logging.save())
            logging.clear();
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

    public void sendServerMessage(final String fmt, final Object... args){
        send(Opcode.SERVER_MESSAGE.create(String.format(fmt, args)));
    }

    public boolean inChannel(final Channel channel){
        return channels.containsKey(channel.getId());
    }

    public void addChannel(final Channel channel){
        channels.put(channel.getId(), channel);
        logging.push(Log.joinChannel(this, channel));
    }

    public void removeChannel(final Channel channel){
        channels.remove(channel.getId());
        logging.push(Log.leaveChannel(this, channel));
    }

    public Collection<Channel> getChannels(){
        return channels.values();
    }

    public Set<Profile> getInteractingProfiles(){
        final Set<Profile> profiles = new HashSet<>();
        profiles.addAll(getFriends().values());
        getChannels().forEach(c -> profiles.addAll(c.getProfiles().values()));
        return profiles;
    }

    public Relationship getRelationship(final Profile profile){
        return relationships.getOrDefault(id, new Relationship(this, profile, Relationship.Type.NONE));
    }

    private boolean isRelationship(final Profile profile, final Relationship.Type type){
        return getRelationship(profile).getType() == type;
    }

    public boolean isFriend(final Profile profile){
        return isRelationship(profile, Relationship.Type.FRIEND);
    }

    public boolean isBlocked(final Profile profile){
        return isRelationship(profile, Relationship.Type.BLOCKED);
    }

    private Map<Integer, Profile> getRelationships(final Relationship.Type type){
        return relationships.values().stream().filter(
                r -> r.getType() == type
        ).collect(Collectors.toMap(Relationship::getTargetId, Relationship::getTarget));
    }

    public Map<Integer, Profile> getFriends(){
        return getRelationships(Relationship.Type.FRIEND);
    }

    public Map<Integer, Profile> getBlocked(){
        return getRelationships(Relationship.Type.BLOCKED);
    }

    private void addRelationship(final Relationship relationship){
        relationships.put(relationship.getTargetId(), relationship);
        logging.push(Log.addRelationship(relationship));
    }

    private void removeRelationship(final Relationship relationship){
        relationships.remove(relationship.getTargetId());
        logging.push(Log.removeRelationship(relationship));
    }

    public boolean addRelationship(final Profile target, final Relationship.Type type, final boolean update){
        final Relationship relationship = new Relationship(this, target, type);
        if(!update){
            addRelationship(relationship);
            return true;
        }
        try(final Relationships relationships = Database.relationships()){
            relationships.insert(relationship);
            addRelationship(relationship);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean removeRelationship(final Relationship relationship, final boolean update){
        if(!update){
            removeRelationship(relationship);
            return true;
        }
        try(final Relationships relationships = Database.relationships()){
            relationships.delete(relationship);
            removeRelationship(relationship);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    protected void load() throws Exception{
        Database.demandRelationships().get(id).forEach(
                r -> relationships.put(r.getTargetId(), r)
        );
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
