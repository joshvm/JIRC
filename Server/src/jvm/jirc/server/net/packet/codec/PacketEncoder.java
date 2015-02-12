package jvm.jirc.server.net.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import jvm.jirc.server.net.packet.Packet;

public class PacketEncoder extends MessageToByteEncoder<Packet>{

    public void encode(final ChannelHandlerContext ctx, final Packet pkt, final ByteBuf out){
        out.writeBytes(pkt.getPayload(), 0, pkt.getLength());
    }
}
