package jvm.jirc.server.sql.log;

import jvm.jirc.server.log.Log;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogMapper implements ResultSetMapper<Log> {

    public Log map(final int idx, final ResultSet rs, final StatementContext ctx) throws SQLException{
        return new Log(
                rs.getTimestamp("timestamp"),
                Log.Type.valueOf(rs.getString("type")),
                rs.getString("entityType"),
                rs.getInt("entityId"),
                rs.getString("info")
        );
    }
}
