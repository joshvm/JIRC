package jvm.jirc.client.entity;

import jvm.jirc.client.entity.event.EntityListener;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    protected final int id;
    private final List<EntityListener> listeners;

    protected Entity(final int id){
        this.id = id;

        listeners = new ArrayList<>();
    }

    public int getId(){
        return id;
    }

    public void addListener(final EntityListener listener){
        listeners.add(listener);
    }

    protected void fireOnUpdate(final int flag){
        listeners.forEach(l -> l.onUpdate(this, flag));
    }

    public void dispose(){
        listeners.clear();
        System.gc();
    }

    public boolean equals(final Object o){
        if(o == null)
            return false;
        if(o == this)
            return true;
        if(!(o instanceof Entity))
            return false;
        return id == ((Entity) o).id;
    }
}
