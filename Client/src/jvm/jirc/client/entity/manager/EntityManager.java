package jvm.jirc.client.entity.manager;

import jvm.jirc.client.entity.Entity;
import jvm.jirc.client.entity.manager.event.EntityManagerListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager<T extends Entity> {

    private final Map<Integer, T> map;
    private final List<EntityManagerListener<T>> listeners;

    public EntityManager(){
        map = new HashMap<>();

        listeners = new ArrayList<>();
    }

    public T get(final int id){
        return map.get(id);
    }

    public Collection<T> get(){
        return map.values();
    }

    public void add(final T entity){
        map.put(entity.getId(), entity);
        fireOnAdd(entity);
    }

    public void remove(final T entity){
        map.remove(entity.getId());
        fireOnRemove(entity);
        entity.dispose();
        System.gc();
    }

    public void clear(){
        get().iterator().forEachRemaining(this::remove);
    }

    public boolean contains(final int id){
        return map.containsKey(id);
    }

    public void addListener(final EntityManagerListener<T> listener){
        listeners.add(listener);
    }

    protected void fireOnAdd(final T entity){
        listeners.forEach(l -> l.onAdd(this, entity));
    }

    protected void fireOnRemove(final T entity){
        listeners.forEach(l -> l.onRemove(this, entity));
    }

}
