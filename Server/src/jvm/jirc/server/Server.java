package jvm.jirc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jvm.jirc.server.entity.manager.ChannelManager;
import jvm.jirc.server.entity.manager.ProfileManager;
import jvm.jirc.server.sql.Database;

public class Server {

    private static final String HOST = "localhost";
    private static final int PORT = 7495;

    private static ProfileManager profileManager;
    private static ChannelManager channelManager;

    private static void start() throws Exception{
        final EventLoopGroup boss = new NioEventLoopGroup();
        final EventLoopGroup worker = new NioEventLoopGroup();
        final ServerBootstrap bs = new ServerBootstrap();
        bs.group(boss, worker);
        bs.channel(NioServerSocketChannel.class);
        bs.childHandler(new ServerInitializer());
        try{
            bs.bind(HOST, PORT).sync().channel().closeFuture().sync();
        }finally{
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static ProfileManager getProfileManager(){
        if(profileManager == null)
            profileManager = new ProfileManager();
        return profileManager;
    }

    public static ChannelManager getChannelManager(){
        if(channelManager == null)
            channelManager = new ChannelManager();
        return channelManager;
    }

    public static void main(String[] args) throws Exception{
        Database.init();
        start();
    }
}
