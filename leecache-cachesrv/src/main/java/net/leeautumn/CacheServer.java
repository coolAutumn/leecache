package net.leeautumn;

import net.leeautumn.config.Configs;
import net.leeautumn.constant.LoggerName;
import net.leeautumn.selectorhandler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LeeAutumn on 11/23/16.
 * blog: leeautumn.net
 */
public class CacheServer implements Runnable{

    static Logger logger = null;


    private ServerSocketChannel putServerSocketChannel;
//    private ServerSocketChannel getServerSocketChannel;

    //不同的Selector代表Socket的不同阶段
    private Selector acceptSelector;
    private Selector readSelector;

    private ExecutorService executorService;

    private Handler handler;

    public CacheServer(Handler handler,int port,String serverName) {
        //初始化监听,线程池.
        //从config模块中读取所需要的参数
        logger = LoggerFactory.getLogger(serverName);
        executorService = Executors.newFixedThreadPool(5);
        this.handler = handler;

        try {

            putServerSocketChannel = ServerSocketChannel.open();
            putServerSocketChannel.configureBlocking(false);
            putServerSocketChannel.socket().bind(new InetSocketAddress(port));

//            getServerSocketChannel = ServerSocketChannel.open();
//            getServerSocketChannel.configureBlocking(false);
//            getServerSocketChannel.socket().bind(new InetSocketAddress(port+1));

            acceptSelector = Selector.open();
            readSelector = Selector.open();

            handler.setReadSelector(readSelector);


            putServerSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            logger.error("Can't open socket port: {}.", String.valueOf(port), e);
        }


        new Thread(handler,Thread.currentThread().getName()+" handler").start();
    }

    public void run() {
        while(true){
            try {
                acceptSelector.select();        //缺少了这句会将CPU吃满
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> serchers = acceptSelector.selectedKeys();
            Iterator<SelectionKey> iterator = serchers.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                handleSerchRequest(selectionKey);
            }
        }
    }

    public void handleSerchRequest(SelectionKey selectionKey){
        SocketChannel clientSocketChannel;
        try {
            if(selectionKey.isAcceptable()){

                //获得监听的serverSocketChannel的通道
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

                //获取客户端进来的连接
                clientSocketChannel = serverSocketChannel.accept();
                clientSocketChannel.configureBlocking(false);                   //设置非阻塞

                clientSocketChannel.register(readSelector,SelectionKey.OP_READ);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Selector getReadSelector() {
        return readSelector;
    }
}
