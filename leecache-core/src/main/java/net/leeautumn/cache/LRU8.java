package net.leeautumn.cache;

import java.util.ArrayList;

/**
 *
 * 此类采用LRU-2的形式来进行
 * 有两个缓存,一个缓存为访问历史记录,一个缓存为高命中的缓存
 * 当访问历史记录中的记录的次数达到threshold时,就把这个缓存放到高命中缓存中.
 * Created by LeeAutumn on 11/21/16.
 * blog: leeautumn.net
 */
public class LRU8 {
//    //高命中缓存
//    static LRUCache<Object> cacheLevel1 = new LRUCache<Object>(25,64,100);
//    //低命中缓存,不允许进行自动修正(lineMaxLength)
//    static LRUCache<Object> cacheLevel2 = new LRUCache<Object>(100,LRUCache.DEFAULT_MAX_CAPACITY,false,LRUCache.DEFAULT_EACHLIST_MAXSIZE);

    private static final int DEFAULT_INIT_LENGTH = 8;

    private static ArrayList<LRUCache<Object>> cachelists = new ArrayList<LRUCache<Object>>();
    private static final Object[]               locks     = new Object[DEFAULT_INIT_LENGTH];
    static {
        for (int i=0;i<DEFAULT_INIT_LENGTH;i++){
            cachelists.add(new LRUCache<Object>(25,64,200));
            locks[i] = new Object();
        }
    }
    public static Object get(String key){
        int h = hashInTable(key);
        Object o = null;
        synchronized (locks[h]) {
            o = cachelists.get(h).get(key);
        }
        return o;
    }

    public static void put(String key,Object value){
        int h = hashInTable(key);
        synchronized (locks[h]) {
            cachelists.get(h).put(key,value);
        }
    }

    public static int getSize(){
        int all = 0;
        for(LRUCache cache : cachelists){
            all += cache.getSize();
        }
        if(all > 25000){
            System.out.println();
        }
        return all;
    }

    private static int hashInTable(String key){
        int h;
        h = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 8);
        return h & DEFAULT_INIT_LENGTH -1 ;
    }
}
