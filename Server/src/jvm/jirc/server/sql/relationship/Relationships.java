package jvm.jirc.server.sql.relationship;

import jvm.jirc.server.entity.profile.Relationship;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

@RegisterMapper(RelationshipMapper.class)
public interface Relationships extends Transactional<Relationships>, AutoCloseable{

    @SqlQuery("SELECT * FROM relationships WHERE fromId = :id")
    List<Relationship> get(@Bind("id") final int id);

    @SqlUpdate("DELETE FROM relationships WHERE fromId = :from AND toId = :to AND type = :type")
    void delete(@BindBean final Relationship relationship);

    @SqlUpdate("INSERT INTO relationships (:type, :from, :to)")
    void insert(@BindBean final Relationship relationship);

    @SqlUpdate("CREATE TABLE IF NOT EXISTS relationships (" +
            "type TEXT NOT NULL, " +
            "fromId INT NOT NULL, " +
            "toId INT NOT NULL" +
            ")")
    void init();

    void close();
}
