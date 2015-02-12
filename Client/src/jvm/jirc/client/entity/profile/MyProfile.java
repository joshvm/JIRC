package jvm.jirc.client.entity.profile;

import jvm.jirc.client.entity.profile.event.MyProfileListener;
import jvm.jirc.client.net.packet.Packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyProfile extends Profile{

    private final Map<Integer, Profile> friends;
    private final Set<Integer> waitingFriends;

    private final List<MyProfileListener> mpListeners;

    public MyProfile(final int id, final String user, final Rank rank, final String name, final Status status){
        super(id, user, rank, name, status);

        friends = new HashMap<>();

        waitingFriends = new HashSet<>();

        mpListeners = new ArrayList<>();
    }

    public Collection<Profile> getProfiles(){
        return friends.values();
    }

    public void addFriend(final Profile profile){
        friends.put(profile.getId(), profile);
    }

    public void removeFriend(final int profileId){
        friends.remove(profileId);
    }

    public boolean isFriend(final int profileId){
        return friends.containsKey(profileId);
    }

    public void addToWaitingFriends(final int profileId){
        waitingFriends.add(profileId);
    }

    public void removeFromWaitingFriends(final int profileId){
        waitingFriends.remove(profileId);
    }

    public boolean isWaitingFriend(final int profileId){
        return waitingFriends.contains(profileId);
    }

    public void addMyProfileListener(final MyProfileListener listener){
        mpListeners.add(listener);
    }

    protected void fireOnFriendAdd(final Profile profile){
        mpListeners.forEach(l -> l.onFriendAdd(profile));
    }

    protected void fireOnFriendRemove(final Profile profile){
        mpListeners.forEach(l -> l.onFriendRemove(profile));
    }

    public void dispose(){
        mpListeners.clear();
        super.dispose();
    }

    public static MyProfile parse(final Packet pkt){
        return new MyProfile(
                pkt.readInt(),
                pkt.readString(),
                Rank.values()[pkt.readByte()],
                pkt.readString(),
                Status.values()[pkt.readByte()]
        );
    }
}
