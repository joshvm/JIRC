package jvm.jirc.server.sql.profile;

import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.entity.profile.Status;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMapper implements ResultSetMapper<Profile> {

    public Profile map(final int i, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new Profile(
                rs.getTimestamp("timestamp"),
                rs.getInt("id"),
                rs.getString("user"),
                rs.getString("pass"),
                Rank.valueOf(rs.getString("rank")),
                rs.getString("name"),
                Status.valueOf(rs.getString("status"))
        );
    }
}
