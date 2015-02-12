package jvm.jirc.server.net.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import jvm.jirc.server.net.packet.Opcode;
import jvm.jirc.server.net.packet.Packet;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder{

    public void decode(final ChannelHandlerContext ctx, final ByteBuf buf, final List<Object> out){
        while(buf.readableBytes() > 0){
            final Opcode opcode = Opcode.get(buf.readByte());
            if(opcode == null)
                continue;
            int length = opcode.getIncomingLength();
            switch(length){
                case -2:
                    length = buf.readShort();
                    break;
                case -1:
                    length = buf.readByte();
                    break;
            }
            if(buf.readableBytes() < length)
                break;
            out.add(new Packet(opcode, buf.readBytes(length)));
        }
    }
}
