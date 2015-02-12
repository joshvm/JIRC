package jvm.jirc.server.sql.channel;

import jvm.jirc.server.entity.channel.ChannelRank;
import jvm.jirc.server.entity.profile.Rank;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelRankMapper implements ResultSetMapper<ChannelRank> {

    public ChannelRank map(final int idx, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new ChannelRank(
                rs.getInt("channelId"),
                rs.getInt("profileId"),
                Rank.valueOf(rs.getString("rank"))
        );
    }
}
