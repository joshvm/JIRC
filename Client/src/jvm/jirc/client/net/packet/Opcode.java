package jvm.jirc.client.net.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum Opcode {

    REGISTER(2, 1, -1),
    LOGIN(3, 1, -1),
    INIT(4, -1),
    UPDATE_RANK(9, 5),
    UPDATE_NAME(8, -1, -1),
    UPDATE_STATUS(10, 5, 1),
    PROFILE_INIT(5, -1, 4);

    private static final Map<Integer, Opcode> MAP = new HashMap<>();

    static{
        Stream.of(values()).forEach(
                o -> MAP.put(o.value, o)
        );
    }

    private final int value;
    private final int incomingLength;
    private final int outgoingLength;

    private Opcode(final int value, final int incomingLength, final int outgoingLength){
        this.value = value;
        this.incomingLength = incomingLength;
        this.outgoingLength = outgoingLength;
    }

    private Opcode(final int value, final int incomingLength){
        this(value, incomingLength, 0);
    }

    public int get(){
        return value;
    }

    public int getIncomingLength(){
        return incomingLength;
    }

    public int getOutgoingLength(){
        return outgoingLength;
    }

    public PacketBuilder builder(){
        return new PacketBuilder(this);
    }

    public static Opcode byValue(final int value){
        return MAP.get(value);
    }
}
