package jvm.jirc.client.entity.channel;

import jvm.jirc.client.entity.Entity;
import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.entity.profile.Profile;
import jvm.jirc.client.entity.profile.Rank;

public class ChannelUser extends Entity{

    public static final int RANK_FLAG = 1 << 1;

    private Rank rank;

    public ChannelUser(final int profileId, final Rank rank){
        super(profileId);
        this.rank = rank;
    }

    public Profile getProfile(){
        return ProfileManager.getInstance().get(id);
    }

    public Rank getRank(){
        return rank;
    }

    public void setRank(final Rank rank){
        this.rank = rank;
        fireOnUpdate(RANK_FLAG);
    }
}
