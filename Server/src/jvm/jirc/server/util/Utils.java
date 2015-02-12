package jvm.jirc.server.util;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

public final class Utils {

    private Utils(){}

    public static Timestamp timestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static String ip(final ChannelHandlerContext ctx){
        return ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString();
    }
}
