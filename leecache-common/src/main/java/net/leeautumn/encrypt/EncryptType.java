package net.leeautumn.encrypt;

/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public enum EncryptType {
    BASE64((byte)0,"base64"),

    MD5((byte)5,"md5");





    private byte code;
    private String name;

    EncryptType(byte c,String name){
        this.code = c;
        this.name = name;
    }

    public EncryptType getEncryptType(byte code){
        for(EncryptType encryptType : EncryptType.values()){
            if(encryptType.getCode() == code){
                return encryptType;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }
}
