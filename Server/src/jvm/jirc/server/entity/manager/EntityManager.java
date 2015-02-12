package jvm.jirc.server.entity.manager;

import jvm.jirc.server.entity.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntityManager<T extends Entity> {

    protected final Map<Integer, T> map;

    protected EntityManager(){
        map = new HashMap<>();
    }

    public Collection<T> getAll(){
        return map.values();
    }

    public void add(final T entity){
        map.put(entity.getId(), entity);
    }

    public void remove(final T entity){
        map.remove(entity.getId());
    }

    public T get(final int id, final boolean init){
        try{
            T entity = map.get(id);
            if(entity == null){
                final List<T> list = sqlGet(id);
                if(!list.isEmpty())
                    entity = list.get(0);
            }
            if(entity == null)
                return null;
            add(entity);
            return (init && init(entity)) || !init ? entity : null;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    protected boolean init(final T entity){
        try{
            return entity.isInitialized() || entity.init();
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    protected abstract List<T> sqlGet(final int id);
}
