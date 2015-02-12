package jvm.jirc.server.entity.manager;

import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.sql.Database;

import java.util.List;

public class ChannelManager extends EntityManager<Channel>{

    protected List<Channel> sqlGet(final int id){
        return Database.demandChannels().get(id);
    }
}
