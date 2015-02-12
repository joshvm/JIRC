package jvm.jirc.server.sql;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import jvm.jirc.server.sql.channel.ChannelRanks;
import jvm.jirc.server.sql.channel.Channels;
import jvm.jirc.server.sql.profile.Profiles;
import jvm.jirc.server.sql.relationship.Relationships;
import org.skife.jdbi.v2.DBI;

public final class Database {

    private static DBI dbi;

    private static Profiles profiles;
    private static Channels channels;
    private static ChannelRanks channelRanks;
    private static Relationships relationships;

    private Database(){}

    public static void init() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        final MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setUrl("jdbc:mysql://localhost/jirc");
        ds.setUser("root");
        ds.setPassword("admin");
        dbi = new DBI(ds);
        try(final Profiles profiles = profiles()){
            profiles.init();
        }
        try(final Relationships relationships = relationships()){
            relationships.init();
        }
        try(final Channels channels = channels()){
            channels.init();
        }
        try(final ChannelRanks channelRanks = channelRanks()){
            channelRanks.init();
        }
    }

    public static Profiles demandProfiles(){
        if(profiles == null)
            profiles = dbi.onDemand(Profiles.class);
        return profiles;
    }

    public static Channels demandChannels(){
        if(channels == null)
            channels = dbi.onDemand(Channels.class);
        return channels;
    }

    public static ChannelRanks demandChannelRanks(){
        if(channelRanks == null)
            channelRanks = dbi.onDemand(ChannelRanks.class);
        return channelRanks;
    }

    public static Relationships demandRelationships(){
        if(relationships == null)
            relationships = dbi.onDemand(Relationships.class);
        return relationships;
    }

    public static Profiles profiles(){
        return dbi.open(Profiles.class);
    }

    public static Channels channels(){
        return dbi.open(Channels.class);
    }

    public static ChannelRanks channelRanks(){
        return dbi.open(ChannelRanks.class);
    }

    public static Relationships relationships(){
        return dbi.open(Relationships.class);
    }

}
