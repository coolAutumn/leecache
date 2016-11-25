package net.leeautumn.selectorhandler;

import net.leeautumn.cache.LRU8;
import net.leeautumn.constant.LoggerName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by LeeAutumn on 11/25/16.
 * blog: leeautumn.net
 */
public class PutCacheHandler extends Handler {
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
                byte[] bytes_name = new byte[16];

                byte[] object     = new byte[byteBuffer.remaining()-16];

                byteBuffer.flip();
                //fill the byte array with the content of bytebuffer
                byteBuffer.get(bytes_name);
                byteBuffer.get(object);

                //response to the client
                LRU8.put(new String(bytes_name, "UTF-8"),object);
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
