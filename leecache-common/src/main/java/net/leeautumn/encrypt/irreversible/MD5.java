package net.leeautumn.encrypt.irreversible;

import net.leeautumn.encrypt.Encrypter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public class MD5 implements Encrypter{

    //md5 encrypt
    public String encrypt(String prime){
        return encrypt(prime.getBytes());
    }

    public String encrypt(byte[] source) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        String encryption="";
        try {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            md5.update(source);
            byte[] md=md5.digest();
            char[] encryption_char=new char[md.length*2];
            int k=0;
            for (int i = 0; i < md.length; i++) {
                byte byte0 = md[i];
                encryption_char[k++] = hexDigits[byte0 >>> 4 & 0xf];
                encryption_char[k++] = hexDigits[byte0 & 0xf];
            }
            encryption=new String(encryption_char);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryption;
    }

    public byte[] decode(String source) {
        return new byte[0];
    }
}
