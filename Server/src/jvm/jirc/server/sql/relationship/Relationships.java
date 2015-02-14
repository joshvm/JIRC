package jvm.jirc.server.sql.relationship;

import jvm.jirc.server.entity.profile.Relationship;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(RelationshipMapper.class)
public interface Relationships extends AutoCloseable{

    @SqlQuery("SELECT * FROM relationships WHERE id = :id")
    List<Relationship> get(@Bind("id") final int id);

    @SqlUpdate("DELETE FROM relationships WHERE id = :id AND targetId = :targetId AND type = :type")
    void delete(@BindBean final Relationship relationship);

    @SqlUpdate("INSERT INTO relationships VALUES (:timestamp, :id, :targetId, :type)")
    void insert(@BindBean final Relationship relationship);

    @SqlUpdate("CREATE TABLE IF NOT EXISTS relationships (" +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "id INT NOT NULL, " +
            "targetId INT NOT NULL, " +
            "type TEXT NOT NULL" +
            ")")
    void init();

    void close();
}
