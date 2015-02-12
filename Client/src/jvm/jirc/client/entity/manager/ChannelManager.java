package jvm.jirc.client.entity.manager;

import jvm.jirc.client.entity.channel.Channel;

public class ChannelManager extends EntityManager<Channel>{

    private static ChannelManager instance;

    public static ChannelManager getInstance(){
        if(instance == null)
            instance = new ChannelManager();
        return instance;
    }
}
