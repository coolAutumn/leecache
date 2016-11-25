package net.leeautumn.selectorhandler;

import net.leeautumn.cache.LRU8;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by LeeAutumn on 11/23/16.
 * blog: leeautumn.net
 */
public class GetCacheHandler extends Handler implements Runnable{
    public void run() {
        while (true){
            try {
                readSelector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> keys = readSelector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isReadable()){
                    handleRequest(selectionKey);
                }
            }
        }
    }

    public void handleRequest(SelectionKey key) {
        SocketChannel clientSocket = (SocketChannel) key.channel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try {
            int readnum = clientSocket.read(byteBuffer);

            if(readnum > 0) {
                //get the info from the buffer
                byte[] bytes = new byte[byteBuffer.remaining()];

                byteBuffer.flip();
                //fill the byte array with the content of bytebuffer
                byteBuffer.get(bytes);

                //response to the client
                byte[] o = LRU8.get(new String(bytes, "UTF-8"));

                byteBuffer.clear();

                byteBuffer.put(o);

                byteBuffer.flip();

                //reponse the net.leeautumn.cache
                clientSocket.write(byteBuffer);
            }
            if(readnum < 0){
                key.cancel();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
