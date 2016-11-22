package net.leeautumn.encrypt.character;

import net.leeautumn.encrypt.Encrypter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public class BASE64 implements Encrypter{
    private static final BASE64Encoder encoder = new BASE64Encoder();
    private static final BASE64Decoder decoder = new BASE64Decoder();

    public String encrypt(byte[] source) {
        return encoder.encodeBuffer(source);
    }

    public byte[] decode(String source) {
        try {
            return decoder.decodeBuffer(source);
        } catch (IOException e) {
            logger.error("BASE64 Encrypt wrong.",e);
        }
        return null;
    }
}
