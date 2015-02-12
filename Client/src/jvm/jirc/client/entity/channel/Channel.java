package jvm.jirc.client.entity.channel;

import jvm.jirc.client.entity.Entity;
import jvm.jirc.client.entity.channel.event.ChannelListener;
import jvm.jirc.client.entity.profile.Rank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Channel extends Entity {

    public static final int DESCRIPTION_FLAG = 1 << 1;

    private final int ownerId;
    private final String name;
    private String description;

    private final Map<Integer, ChannelUser> users;
    private final Map<Integer, Rank> waiting;

    private final List<ChannelListener> cListeners;

    public Channel(final int id, final int ownerId, final String name, final String description){
        super(id);
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;

        users = new HashMap<>();

        waiting = new HashMap<>();

        cListeners = new ArrayList<>();
    }

    public int getOwnerId(){
        return ownerId;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(final String description){
        this.description = description;
        fireOnUpdate(DESCRIPTION_FLAG);
    }

    public Collection<ChannelUser> getUsers(){
        return users.values();
    }

    public void add(final ChannelUser user){
        users.put(user.getId(), user);
        fireOnJoin(user);
    }

    public void remove(final int profileId){
        final ChannelUser user = users.remove(profileId);
        if(user != null)
            fireOnLeave(user);
    }

    public boolean contains(final int profileId){
        return users.containsKey(profileId);
    }

    public void addToWaiting(final int profileId, final Rank rank){
        waiting.put(profileId, rank);
    }

    public Rank removeFromWaiting(final int profileId){
        return waiting.remove(profileId);
    }

    public boolean isWaiting(final int profileId){
        return waiting.containsKey(profileId);
    }

    public void addChannelListener(final ChannelListener listener){
        cListeners.add(listener);
    }

    protected void fireOnJoin(final ChannelUser user){
        cListeners.forEach(l -> l.onJoin(this, user));
    }

    protected void fireOnLeave(final ChannelUser user){
        cListeners.forEach(l -> l.onLeave(this, user));
    }

    public void dispose(){
        cListeners.clear();
        super.dispose();
    }
}
