package jvm.jirc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import jvm.jirc.server.net.packet.codec.PacketDecoder;
import jvm.jirc.server.net.packet.codec.PacketEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    public void initChannel(final SocketChannel ch){
        ch.pipeline().addLast("encoder", new PacketEncoder());
        ch.pipeline().addLast("decoder", new PacketDecoder());
        ch.pipeline().addLast("handler", new ServerHandler());
    }
}
