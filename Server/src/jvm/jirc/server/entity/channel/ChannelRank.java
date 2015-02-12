package jvm.jirc.server.entity.channel;

import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.sql.Database;
import jvm.jirc.server.sql.channel.ChannelRanks;

public class ChannelRank {

    private int channelId;
    private int profileId;
    private Rank rank;

    public ChannelRank(final int channelId, final int profileId, final Rank rank){
        this.channelId = channelId;
        this.profileId = profileId;
        this.rank = rank;
    }

    public ChannelRank(){
        this(-1, -1, null);
    }

    public int getChannelId(){
        return channelId;
    }

    public int getProfileId(){
        return profileId;
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
        try(final ChannelRanks channelRanks = Database.channelRanks()){
            channelRanks.updateRank(this);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            this.rank = oldRank;
            return false;
        }
    }

    public void load() throws Exception{}
}
