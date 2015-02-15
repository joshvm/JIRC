package jvm.jirc.server.log;

import jvm.jirc.server.entity.Entity;
import jvm.jirc.server.sql.Database;
import jvm.jirc.server.sql.log.Logs;

import java.util.ArrayList;
import java.util.List;

public class Logging {

    private static final int LIMIT = 100;

    private final Entity entity;
    private final List<Log> logs;

    public Logging(final Entity entity){
        this.entity = entity;

        logs = new ArrayList<>();
    }

    public Entity getEntity(){
        return entity;
    }

    public String getEntityType(){
        return entity.getClass().getSimpleName();
    }

    public void clear(){
        logs.clear();
    }

    public void push(final Log log){
        logs.add(log);
        if(logs.size() < LIMIT)
            return;
        final List<Log> batch = logs.subList(0, 100);
        if(saveBatch(batch))
            logs.removeAll(batch);
    }

    public boolean save(){
        return saveBatch(logs);
    }

    private boolean saveBatch(final List<Log> batch){
        Logs logs = null;
        try{
            logs = Database.logs();
            logs.begin();
            logs.insert(batch);
            logs.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            if(logs != null)
                logs.rollback();
            return false;
        }finally{
            if(logs != null)
                logs.close();
        }
    }

}
