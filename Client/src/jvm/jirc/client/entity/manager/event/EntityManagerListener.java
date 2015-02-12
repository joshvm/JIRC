package jvm.jirc.client.entity.manager.event;

import jvm.jirc.client.entity.Entity;
import jvm.jirc.client.entity.manager.EntityManager;

public interface EntityManagerListener<T extends Entity> {

    public void onAdd(final EntityManager<T> manager, final T entity);

    public void onRemove(final EntityManager<T> manager, final T entity);
}
