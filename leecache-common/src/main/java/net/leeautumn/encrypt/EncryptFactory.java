package net.leeautumn.encrypt;

import net.leeautumn.constant.LoggerName;
import net.leeautumn.encrypt.character.*;
import net.leeautumn.encrypt.irreversible.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public class EncryptFactory {
    private static Logger logger = LoggerFactory.getLogger(LoggerName.EncryptFactoryLoggerName);

    /**
     * Enctypters
     */
    private static final Encrypter base64 = new BASE64();
    private static final Encrypter md5    = new MD5();

    public static String encrypt(byte[] source,EncryptType encryptType){
        String result = null;

        switch (encryptType){
            case BASE64:
                result = base64.encrypt(source);
                break;
            case MD5:
                result = md5.encrypt(source);
                break;
            default:
                logger.warn("Temporary not supported encrypt way :{0}",encryptType.toString());
        }
        return result;
    }

    public static byte[] decode(String source,EncryptType encryptType){
        byte[] result = null;

        switch (encryptType){
            case BASE64:
                result = base64.decode(source);
                break;
            case MD5:
                result = md5.decode(source);
                break;
            default:
                logger.warn("Temporary not supported decode way :{0}",encryptType.toString());
        }

        return result;
    }
}
