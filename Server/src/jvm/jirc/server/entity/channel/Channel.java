package jvm.jirc.server.entity.channel;

import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.log.Log;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;
import jvm.jirc.server.sql.Database;
import jvm.jirc.server.sql.channel.Channels;
import jvm.jirc.server.util.Utils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Channel extends Entity{

    private String name;
    private int ownerId;
    private String ownerUser;
    private String description;

    private final Map<Integer, ChannelRank> ranks;
    private final Map<Integer, Profile> profiles;

    public Channel(final Timestamp timestamp, final int id, final String name, final int ownerId, final String ownerUser, final String description){
        super(timestamp, id);
        this.name = name;
        this.ownerId = ownerId;
        this.ownerUser = ownerUser;
        this.description = description;

        ranks = new HashMap<>();
        profiles = new HashMap<>();
    }

    public Channel(final String name, final int ownerId, final String ownerUser, final String description){
        this(Utils.timestamp(), (int)System.nanoTime(), name, ownerId, ownerUser, description);
    }

    public Channel(final String name, final Profile owner, final String description){
        this(name, owner.getId(), owner.getUser(), description);
    }

    public Channel(){
        this(null, -1, null, null);
    }

    public String getName(){
        return name;
    }

    public int getOwnerId(){
        return ownerId;
    }

    public String getOwnerUser(){
        return ownerUser;
    }

    public String getDescription(){
        return description;
    }

    public boolean setDescription(final String description, final boolean update){
        final String oldDescription = this.description;
        this.description = description;
        if(!update){
            logging.push(Log.changeChannelDescription(this, oldDescription, description));
            return true;
        }
        try(final Channels channels = Database.channels()){
            channels.updateDescription(this);
            logging.push(Log.changeChannelDescription(this, oldDescription, description));
            return true;
        }catch(Exception ex){
            this.description = oldDescription;
            ex.printStackTrace();
            return false;
        }
    }

    public Packet toPacket(final Opcode opcode){
        return opcode.builder().writeChannel(this).toPacket();
    }

    public Map<Integer, Profile> getProfiles(){
        return profiles;
    }

    public boolean contains(final Profile profile){
        return profiles.containsKey(profile.getId());
    }

    public void join(final Profile profile){
        profile.addChannel(this);
        logging.push(Log.joinChannel(this, profile));
        send(Opcode.JOIN_CHANNEL.create(profile));
    }

    public void leave(final Profile profile){
        profile.removeChannel(this);
        logging.push(Log.leaveChannel(this, profile));
        final Packet out = Opcode.LEAVE_CHANNEL.create(profile);
        profile.send(out);
        send(out);
    }

    public Rank getRank(final int profileId){
        return getChannelRank(profileId).getRank();
    }

    public ChannelRank getChannelRank(final int profileId){
        return ranks.getOrDefault(profileId, new ChannelRank(id, profileId, Rank.NONE));
    }

    public void addChannelRank(final ChannelRank rank){
        ranks.put(rank.getProfileId(), rank);
    }

    public void removeChannelRank(final ChannelRank rank){
        ranks.remove(rank.getProfileId());
    }

    public void message(final Profile profile, final String msg){
        profile.getLogging().push(Log.channelMessage(profile, this, msg));
        logging.push(Log.message(this, profile, msg));
        send(Opcode.CHANNEL_MESSAGE.create(this, profile, msg));
    }

    public void sendExcept(final Packet pkt, final Profile exception){
        profiles.values().stream().filter(
                p -> !p.equals(exception)
        ).forEach(p -> p.send(pkt));
    }

    public void send(final Packet pkt){
        sendExcept(pkt, null);
    }

    protected void load() throws Exception{
        final List<ChannelRank> list = Database.demandChannelRanks().get(id);
        list.forEach(this::addChannelRank);
    }
}
