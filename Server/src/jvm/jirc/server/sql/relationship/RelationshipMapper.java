package jvm.jirc.server.sql.relationship;

import jvm.jirc.server.entity.profile.Relationship;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationshipMapper implements ResultSetMapper<Relationship> {

    public Relationship map(final int idx, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new Relationship(
                rs.getTimestamp("timestamp"),
                rs.getInt("id"),
                rs.getInt("targetId"),
                Relationship.Type.valueOf(rs.getString(""))
        );
    }
}
