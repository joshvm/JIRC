package jvm.jirc.server.sql.profile;

import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.entity.profile.Rank;
import jvm.jirc.server.entity.profile.Status;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(ProfileMapper.class)
public interface Profiles extends AutoCloseable{

    @SqlQuery("SELECT * FROM profiles WHERE user = :user")
    List<Profile> get(@Bind("user") final String user);

    @SqlQuery("SELECT * FROM profiles WHERE id = :id")
    List<Profile> get(@Bind("id") final int id);

    @SqlUpdate("UPDATE profiles SET pass = :pass, rank = :rank, name = :name, status = :status WHERE id = :id")
    void update(@BindBean final Profile profile);

    @SqlUpdate("UPDATE profiles SET pass = :pass WHERE id = :id")
    void updatePass(@BindBean final Profile profile);

    @SqlUpdate("UPDATE profiles SET rank = :rank WHERE id = :id")
    void updateRank(@BindBean final Profile profile);

    @SqlUpdate("UPDATE profiles SET name = :name WHERE id = :id")
    void updateName(@BindBean final Profile profile);

    @SqlUpdate("UPDATE profiles SET status = :status WHERE id = :id")
    void updateStatus(@BindBean final Profile profile);

    @SqlUpdate("DELETE FROM profiles WHERE id = :id")
    void delete(@BindBean final Profile profile);

    @SqlUpdate("INSERT INTO profiles (user, pass, rank, name, status) VALUES (:user, :pass, :rank, :name, :status)")
    void insert(@Bind("user") final String user, @Bind("pass") final String pass, @Bind("rank") final Rank rank, @Bind("name") final String name, @Bind("status") final Status status);

    @SqlUpdate("CREATE TABLE IF NOT EXISTS profiles (" +
            "timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "id INT NOT NULL AUTO_INCREMENT, " +
            "user TEXT NOT NULL, " +
            "pass TEXT NOT NULL, " +
            "rank TEXT NOT NULL, " +
            "name TEXT NOT NULL, " +
            "status TEXT NOT NULL, " +
            "PRIMARY KEY (id)" +
            ")")
    void init();

    void close();
}
