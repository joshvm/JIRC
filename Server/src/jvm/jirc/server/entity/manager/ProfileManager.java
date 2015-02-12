package jvm.jirc.server.entity.manager;

import jvm.jirc.server.entity.profile.Profile;
import jvm.jirc.server.sql.Database;

import java.util.List;

public class ProfileManager extends EntityManager<Profile>{

    public Profile get(final String user, final boolean init){
        try{
            final Profile p = getAll().stream().filter(
                    o -> o.getUser().equalsIgnoreCase(user)
            ).findFirst().orElseGet(() -> sqlGet(user));
            if(p == null)
                return null;
            add(p);
            return (init && init(p)) || !init ? p : null;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    protected Profile sqlGet(final String user){
        final List<Profile> list = Database.demandProfiles().get(user);
        return list.isEmpty() ? null : list.get(0);
    }

    protected List<Profile> sqlGet(final int id){
        return Database.demandProfiles().get(id);
    }
}
