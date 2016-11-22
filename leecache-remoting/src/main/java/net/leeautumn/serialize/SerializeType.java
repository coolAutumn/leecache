package net.leeautumn.serialize;

/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public enum SerializeType {
    JSON((byte)0);

    private byte code;

    SerializeType(byte code){
        this.code = code;
    }

    public static SerializeType valueOf(byte code) {
        for (SerializeType serializeType : SerializeType.values()) {
            if (serializeType.getCode() == code) {
                return serializeType;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }
}
