package jvm.jirc.server.net.packet;

import jvm.jirc.server.entity.channel.Channel;
import jvm.jirc.server.entity.profile.Profile;

import java.util.HashMap;
import java.util.Map;

public enum Opcode {

    POPUP_MESSAGE(0, -1){
        public Packet create(final Object... args){
            final String msg = (String) args[0];
            return builder().writeString(msg).toPacket();
        }
    },
    SERVER_MESSAGE(1, -1){
        public Packet create(final Object... args){
            final String msg = (String) args[0];
            return builder().writeString(msg).toPacket();
        }
    },
    REGISTER(2, -1, 1){
        public Packet create(final Object... args){
            final int response = (Integer) args[0];
            return builder().writeByte(response).toPacket();
        }
    },
    LOGIN(3, -1, 1){
        public Packet create(final Object... args){
            final int response = (Integer) args[0];
            return builder().writeByte(response).toPacket();
        }
    },
    INIT(4, -1){
        public Packet create(final Object... args){
            final Profile profile = (Profile) args[0];
            return profile.toPacket(this);
        }
    },
    PROFILE_INIT(5, 4, -1){
        public Packet create(final Object... args){
            final Profile profile = (Profile) args[0];
            return profile.toPacket(this);
        }
    },
    ADD_FRIEND(6, 4, -1){
        public Packet create(final Object... args){
            final Profile profile = (Profile) args[0];
            return profile.toPacket(this);
        }
    },
    REMOVE_FRIEND(7, 4, 4){
        public Packet create(final Object... args){
            final Profile profile = (Profile) args[0];
            return builder().writeInt(profile.getId()).toPacket();
        }
    },
    UPDATE_NAME(8, -1, -1){
        public Packet create(final Object... args){
            final Profile profile = (Profile) args[0];
            return builder().writeInt(profile.getId())
                    .writeString(profile.getName())
                    .toPacket();
        }
    },
    UPDATE_RANK(9, 5){
        public Packet create(final Object... args){
            final Profile profile = (Profile) args[0];
            return builder().writeInt(profile.getId())
                    .writeByte(profile.getRank().ordinal())
                    .toPacket();
        }
    },
    UPDATE_STATUS(10, 1, 5){
        public Packet create(final Object... args){
            final Profile profile = (Profile) args[0];
            return builder().writeInt(profile.getId())
                    .writeByte(profile.getStatus().ordinal())
                    .toPacket();
        }
    },
    MESSAGE(11, -2, -2){
        public Packet create(final Object... args){
            final Profile from = (Profile) args[0];
            final Profile to = (Profile) args[1];
            final String msg = (String) args[2];
            return builder().writeInt(from.getId())
                    .writeInt(to.getId())
                    .writeString(msg)
                    .toPacket();
        }
    },
    CHANNEL_INIT(12, 4, -1){
        public Packet create(final Object... args){
            final Channel channel = (Channel) args[0];
            return channel.toPacket(this);
        }
    },
    JOIN_CHANNEL(13, 4, 9){
        public Packet create(final Object... args){
            final Channel channel = (Channel) args[0];
            final Profile profile = (Profile) args[1];
            return builder().writeInt(channel.getId())
                    .writeInt(profile.getId())
                    .writeByte(channel.getRank(profile.getId()).ordinal())
                    .toPacket();
        }
    },
    CHANNEL_UPDATE_RANK(14, 9){
        public Packet create(final Object... args){
            final Channel channel = (Channel) args[0];
            final Profile profile = (Profile) args[1];
            return builder().writeInt(channel.getId())
                    .writeInt(profile.getId())
                    .writeByte(channel.getRank(profile.getId()).ordinal())
                    .toPacket();
        }
    },
    LEAVE_CHANNEL(15, 4, 4){
        public Packet create(final Object... args){
            final Channel channel = (Channel) args[0];
            final Profile profile = (Profile) args[1];
            return builder().writeInt(channel.getId())
                    .writeInt(profile.getId())
                    .toPacket();
        }
    },
    CHANNEL_MESSAGE(16, -2, -2){
        public Packet create(final Object... args){
            final Channel channel = (Channel) args[0];
            final Profile profile = (Profile) args[1];
            final String msg = (String) args[2];
            return builder().writeInt(channel.getId())
                    .writeInt(profile.getId())
                    .writeString(msg)
                    .toPacket();
        }
    };

    private static final Map<Integer, Opcode> MAP = new HashMap<>();

    static{
        for(final Opcode o : values())
            MAP.put(o.getValue(), o);
    }

    private final int value;
    private final int incomingLength;
    private final int outgoingLength;

    private Opcode(final int value, final int incomingLength, final int outgoingLength){
        this.value = value;
        this.incomingLength = incomingLength;
        this.outgoingLength = outgoingLength;
    }

    private Opcode(final int value, final int outgoingLength){
        this(value, 0, outgoingLength);
    }

    public int getValue(){
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

    public abstract Packet create(final Object... args);

    public static Opcode get(final int opcode){
        return MAP.get(opcode);
    }
}
