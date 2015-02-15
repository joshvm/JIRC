package jvm.jirc.server.sql.log;

import jvm.jirc.server.log.Log;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.Collection;

@RegisterMapper(LogMapper.class)
public interface Logs extends Transactional<Logs>, AutoCloseable {

    @SqlBatch("INSERT INTO logs VALUES (:timestamp, :entityType, :entityId, :type, :info)")
    @BatchChunkSize(150)
    void insert(@BindBean final Collection<Log> logs);

    @SqlUpdate("CREATE TABLE IF NOT EXISTS logs (" +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "entityType TEXT NOT NULL, " +
            "entityId INT NOT NULL, " +
            "type TEXT NOT NULL, " +
            "info TEXT NOT NULL" +
            ")")
    void init();

    void close();
}
