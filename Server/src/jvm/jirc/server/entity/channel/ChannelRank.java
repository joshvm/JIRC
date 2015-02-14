package jvm.jirc.server.entity.channel;

import jvm.jirc.server.Server;
import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.log.Log;
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

    public Channel getChannel(){
        return Server.getChannelManager().get(channelId, false);
    }

    public int getProfileId(){
        return profileId;
    }

    public Profile getProfile(){
        return Server.getProfileManager().get(profileId, false);
    }

    public Rank getRank(){
        return rank;
    }

    public boolean setRank(final Rank rank, final boolean update){
        final Rank oldRank = this.rank;
        this.rank = rank;
        if(!update){
            getChannel().getLogging().push(Log.changeChannelRank(getChannel(), getProfile(), oldRank, rank));
            return true;
        }
        try(final ChannelRanks channelRanks = Database.channelRanks()){
            if(oldRank == Rank.NONE){
                channelRanks.insert(this);
                getChannel().addChannelRank(this);
            }else if(rank == Rank.NONE){
                channelRanks.delete(this);
                getChannel().removeChannelRank(this);
            }else
                channelRanks.updateRank(this);
            getChannel().getLogging().push(Log.changeChannelRank(getChannel(), getProfile(), oldRank, rank));
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            this.rank = oldRank;
            return false;
        }
    }

    public void load() throws Exception{}
}
