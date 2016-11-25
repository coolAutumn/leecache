package net.leeautumn;

import net.leeautumn.config.Configs;
import net.leeautumn.selectorhandler.GetCacheHandler;
import net.leeautumn.selectorhandler.Handler;
import net.leeautumn.selectorhandler.PutCacheHandler;

/**
 * Created by LeeAutumn on 11/25/16.
 * blog: leeautumn.net
 */
public class ServerStart {
    public static void main(String[] args) {
        Handler get = new GetCacheHandler();
        CacheServer getCacheServer = new CacheServer(get, Integer.valueOf(Configs.get("leecache.server.getCacheServer.port")),"GetCacheServer");

        Handler put = new PutCacheHandler();
        CacheServer putCacheServer = new CacheServer(put,Integer.valueOf(Configs.get("leecache.server.putCacheServer.port")),"PutCacheServer");

        new Thread(getCacheServer,"GetCacheServer").start();
        new Thread(putCacheServer,"PutCacheServer").start();
    }
}
