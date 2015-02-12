package jvm.jirc.server.entity.profile;

public enum Status {

    ONLINE,
    BUSY,
    AWAY,
    OFFLINE;

    public static Status forIndex(final int idx){
        return idx > -1 && idx < 4 ? values()[idx] : null;
    }
}
