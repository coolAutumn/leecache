package net.leeautumn.encrypt;

import net.leeautumn.constant.LoggerName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public interface Encrypter {
    String Logger = LoggerName.EncryptLoggerName;
    Logger logger = LoggerFactory.getLogger(Logger);

    public String encrypt(byte[] source);

    public byte[] decode (String source);
}
