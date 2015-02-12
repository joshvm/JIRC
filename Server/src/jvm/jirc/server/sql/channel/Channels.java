package jvm.jirc.server.sql.channel;

import jvm.jirc.server.entity.channel.Channel;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

@RegisterMapper(ChannelMapper.class)
public interface Channels extends Transactional<Channels>, AutoCloseable{

    @SqlQuery("SELECT * FROM channels WHERE id = :id")
    List<Channel> get(@Bind("id") final int id);

    @SqlUpdate("INSERT INTO channels VALUES (:name:, :ownerId, :ownerUser, :description)")
    void insert(@BindBean final Channel channel);

    @SqlUpdate("DELETE FROM channels WHERE id = :id")
    void delete(@BindBean final Channel channel);

    @SqlUpdate("UPDATE channels SET description = :description WHERE id = :id")
    void updateDescription(@BindBean final Channel channel);

    @SqlUpdate("SELECT COUNT(1) FROM channels")
    int count();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS channels (" +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "name TEXT NOT NULL, " +
            "ownerId INT NOT NULL, " +
            "ownerUser TEXT NOT NULL, " +
            "description TEXT NOT NULL," +
            "PRIMARY KEY (id)" +
            ")")
    void init();

    void close();
}
