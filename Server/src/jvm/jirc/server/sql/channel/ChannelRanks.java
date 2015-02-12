package jvm.jirc.server.sql.channel;

import jvm.jirc.server.entity.channel.ChannelRank;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

@RegisterMapper(ChannelRankMapper.class)
public interface ChannelRanks extends Transactional<ChannelRanks>, AutoCloseable{

    @SqlQuery("SELECT * FROM channelranks WHERE channelId = :channelId")
    List<ChannelRank> get(@Bind("channelId") final int channelId);

    @SqlUpdate("UPDATE channelranks SET rank = :rank WHERE channelId = :channelId AND profileId = :profileId")
    void updateRank(@BindBean final ChannelRank cr);

    @SqlUpdate("INSERT INTO channelranks VALUES (:channelId, :profileId, :rank)")
    void insert(@BindBean final ChannelRank cr);

    @SqlUpdate("DELETE FROM channelranks WHERE channelId = :channelId AND profileId = :profileId AND rank = :rank")
    void delete(@BindBean final ChannelRank cr);

    @SqlUpdate("CREATE TABLE IF NOT EXISTS channelranks (" +
            "channelId INT NOT NULL, " +
            "profileId INT NOT NULL, " +
            "rank TEXT NOT NULL" +
            ")")
    void init();

    void close();
}
