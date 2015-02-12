package jvm.jirc.server.entity.channel;

import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;
import jvm.jirc.server.sql.Database;
import jvm.jirc.server.sql.channel.ChannelRanks;
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
        if(!update){
            this.description = description;
            return true;
        }
        final String oldDescription = this.description;
        this.description = description;
        try(final Channels channels = Database.channels()){
            channels.updateDescription(this);
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

    public void join(final Profile profile){
        profile.addChannel(this);
    }

    public void leave(final Profile profile){
        profile.removeChannel(this);
    }

    public boolean addChannelRank(final int profileId, final Rank rank, final boolean update){
        final ChannelRank channelRank = new ChannelRank(id, profileId, rank);
        addChannelRank(channelRank);
        if(!update)
            return true;
        try(final ChannelRanks channelRanks = Database.channelRanks()){
            channelRanks.insert(channelRank);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            removeChannelRank(channelRank);
            return false;
        }
    }

    public boolean removeChannelRank(final int profileId, final boolean update){
        final ChannelRank channelRank = ranks.get(profileId);
        removeChannelRank(channelRank);
        if(!update)
            return true;
        try(final ChannelRanks channelRanks = Database.channelRanks()){
            channelRanks.delete(channelRank);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            addChannelRank(channelRank);
            return false;
        }
    }

    public Rank getRank(final int profileId){
        final ChannelRank rank = getChannelRank(profileId);
        return rank == null ? Rank.NONE : rank.getRank();
    }

    public ChannelRank getChannelRank(final int profileId){
        return ranks.get(profileId);
    }

    private void addChannelRank(final ChannelRank rank){
        ranks.put(rank.getProfileId(), rank);
    }

    private void removeChannelRank(final ChannelRank rank){
        ranks.remove(rank.getProfileId());
    }

    public void sendExcept(final Packet pkt, final Profile exception){
        profiles.values().stream().filter(
                p -> !p.equals(exception)
        ).forEach(p -> p.send(pkt));
    }

    public void send(final Packet pkt){
        sendExcept(pkt, null);
    }

    public void load() throws Exception{
        final List<ChannelRank> list = Database.demandChannelRanks().get(id);
        list.forEach(this::addChannelRank);
    }
}
