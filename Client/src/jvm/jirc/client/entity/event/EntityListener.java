package jvm.jirc.client.entity.event;

import jvm.jirc.client.entity.Entity;

public interface EntityListener<T extends Entity> {

    public void onUpdate(final T entity, final int flag);
}
