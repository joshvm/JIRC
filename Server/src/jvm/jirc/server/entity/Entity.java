package jvm.jirc.server.entity;

import jvm.jirc.server.log.Logging;

import java.sql.Timestamp;
import java.util.Date;

public abstract class Entity {

    protected int id;
    protected Timestamp timestamp;

    private boolean initialized;

    protected final Logging logging;

    protected Entity(final Timestamp timestamp, final int id){
        this.timestamp = timestamp;
        this.id = id;

        logging = new Logging(this);
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public Date getDate(){
        return new Date(timestamp.getTime());
    }

    public int getId(){
        return id;
    }

    public boolean isInitialized(){
        return initialized;
    }

    public boolean init(){
        try{
            load();
            return initialized = true;
        }catch(Exception ex){
            ex.printStackTrace();
            return initialized = false;
        }
    }

    public Logging getLogging(){
        return logging;
    }

    public boolean equals(final Object o){
        if(o == null)
            return false;
        if(o == this)
            return true;
        if(!getClass().equals(o.getClass()))
            return false;
        return id == ((Entity)o).id;
    }

    protected abstract void load() throws Exception;
}
