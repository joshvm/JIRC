package jvm.jirc.client.entity.channel.event;

import jvm.jirc.client.entity.channel.Channel;
import jvm.jirc.client.entity.channel.ChannelUser;

public interface ChannelListener {

    public void onJoin(final Channel channel, final ChannelUser user);

    public void onLeave(final Channel channel, final ChannelUser user);
}
