package jvm.jirc.client.entity.manager;

import jvm.jirc.client.entity.channel.ChannelUser;
import jvm.jirc.client.entity.profile.Profile;
import jvm.jirc.client.entity.profile.Rank;
import jvm.jirc.client.entity.profile.MyProfile;

public class ProfileManager extends EntityManager<Profile>{

    private static ProfileManager instance;

    private MyProfile myProfile;

    public void setMyProfile(final MyProfile myProfile){
        this.myProfile = myProfile;
        add(myProfile);
    }

    public MyProfile getMyProfile(){
        return myProfile;
    }

    public void add(final Profile profile){
        super.add(profile);
        if(myProfile != null && myProfile.isWaitingFriend(profile.getId())){
            myProfile.removeFromWaitingFriends(profile.getId());
            myProfile.addFriend(profile);
        }
        ChannelManager.getInstance().get().stream().filter(
                c -> c.isWaiting(profile.getId())
        ).forEach(
                c -> {
                    final Rank rank = c.removeFromWaiting(profile.getId());
                    if(rank == null) //this check is pointless - could never be too sure though
                        return;
                    c.add(new ChannelUser(profile.getId(), rank));
                }
        );
    }

    public void clear(){
        super.clear();
        myProfile = null;
    }

    public static ProfileManager getInstance(){
        if(instance == null)
            instance = new ProfileManager();
        return instance;
    }
}
