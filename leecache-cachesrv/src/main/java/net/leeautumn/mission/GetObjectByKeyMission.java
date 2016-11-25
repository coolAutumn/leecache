package net.leeautumn.mission;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by LeeAutumn on 11/23/16.
 * blog: leeautumn.net
 */
public class GetObjectByKeyMission implements Callable<byte[]>{

    SocketChannel socketChannel;

    GetObjectByKeyMission(SocketChannel socketChannel){

    }

    public byte[] call() throws Exception {
        return null;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
}
