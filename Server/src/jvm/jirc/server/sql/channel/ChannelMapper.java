package jvm.jirc.server.sql.channel;

import jvm.jirc.server.entity.channel.Channel;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelMapper implements ResultSetMapper<Channel> {

    public Channel map(final int idx, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new Channel(
                rs.getTimestamp("timestamp"),
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("ownerId"),
                rs.getString("ownerUser"),
                rs.getString("description")
        );
    }
}
