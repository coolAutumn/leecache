package net.leeautumn.inet;

import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;

/**
 * Created by LeeAutumn on 11/20/16.
 * blog: leeautumn.net
 */
@Deprecated
public class NetworkAddress {
    private static String localhost = null;
    private static Map<String,String[]> inetAddressCache_Key_Host = new HashMap<String,String[]>(4);
    private static Map<String,String> inetAddressCache_Key_Ip = new HashMap<String,String>(4);

    /**
     * Get ip by hostname
     * Even if you can set ip address as the param, better not try.
     * @param hostname
     * @return
     * @throws UnknownHostException
     */
    public synchronized static String[] getIpByName(String hostname) throws UnknownHostException {
        String[] s = inetAddressCache_Key_Host.get(hostname);
        if(s != null){
            return s;
        }
        InetAddress[] ips = InetAddress.getAllByName(hostname);
        s = new String[ips.length];
        for(int i = 0; i< ips.length; i++){
            s[i] = ips[i].getHostAddress();
        }
        inetAddressCache_Key_Host.put(hostname,s);
        return s;
    }

    /**
     * get hostname by ip
     * @param ip
     * @return
     */
    public synchronized static String getNameByIp(String ip) throws UnknownHostException {
        String s = inetAddressCache_Key_Ip.get(ip);
        if(s != null){
            return s;
        }
        s = InetAddress.getByName(ip).getHostName();
        inetAddressCache_Key_Ip.put(ip,s);
        return s;
    }

    /**
     *
     * @return
     */
    public synchronized static String getLocalhostIp() throws UnknownHostException {
        if(localhost != null){
            return localhost;
        }
        localhost = new String(InetAddress.getLocalHost().getAddress());
        return localhost;
    }

//    private static String convertByteArrayToIp(byte[] bytes){
//        boolean isIpv4 = bytes.length == 4;
//        boolean isIpv6 = bytes.length == ;
//
//        if(bytes == null || bytes.length != 4){
//            throw new IllegalArgumentException("Illegal ip bytes.");
//        }
//        StringBuilder sb = new StringBuilder();
//        for(byte b : bytes){
//            sb.append(b);
//        }
//    }
//
    private static byte[] convertStringToBytes(String ip){
        if(IPAddressUtil.isIPv4LiteralAddress(ip)){
            return IPAddressUtil.textToNumericFormatV4(ip);
        }
        if(IPAddressUtil.isIPv6LiteralAddress(ip)){
            return IPAddressUtil.textToNumericFormatV6(ip);
        }
        throw new IllegalArgumentException("Illegal ip address.");

    }
}
