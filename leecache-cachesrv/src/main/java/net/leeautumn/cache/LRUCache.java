package net.leeautumn.cache;

import java.util.HashSet;

/**
 * 这是leecache的cache实现中的LRUCache.
 *
 * Created by LeeAutumn on 11/19/16.
 *
 * @author Leeautumn
 *
 * blog: leeautumn.net
 */
public class LRUCache<V> {

    private CacheNode[] table;
    private final Object[]    locks;
    private HashSet<String>   existsKey;

    private volatile int size = 0;
    private final Object sizeLock = new Object();

    private volatile int capacity = DEFAULT_INIT_CAPACITY;  //Hash桶的大小

    private final int maxCapacity;                          //Hash桶的长度最大值

    private final int eachListMaxLength;                          //Hash桶的长度最大值

    private int[] listsLength;

    private boolean autoUpdateListLength = false;

    /**
     * Default config paramters
     */
    public static final int DEFAULT_MAX_CAPACITY  = 2 << 7;
    public static final int DEFAULT_INIT_CAPACITY = 2 << 4;
    public static final int DEFAULT_EACHLIST_MAXSIZE     = 2 << 7;      //256

    static class CacheNode<V>{
        String  key;
        V       value;
        CacheNode<V> next = null;     //单向链表足以

        public CacheNode(String key,V value){
            this.key    =   key;
            this.value  =   value;
        }
    }

    public LRUCache(){
        this(DEFAULT_INIT_CAPACITY,DEFAULT_MAX_CAPACITY,DEFAULT_EACHLIST_MAXSIZE);
    }
    public LRUCache(int capacity){
        this(tableSizeFor(capacity),DEFAULT_MAX_CAPACITY,DEFAULT_EACHLIST_MAXSIZE);
    }

    public LRUCache(int capacity, int maxCapacity,int eachListMaxLength){
        this.capacity = tableSizeFor(capacity);
        this.maxCapacity = tableSizeFor(maxCapacity);
        this.eachListMaxLength = eachListMaxLength;

        table = new CacheNode[this.capacity];
        listsLength = new int[this.capacity];
        locks = new Object[this.capacity];
        Init();
    }

    public LRUCache(int capacity, int maxCapacity , boolean autoUpdateListLength , int eachListMaxLength){
        this.capacity = tableSizeFor(capacity);
        this.maxCapacity = tableSizeFor(maxCapacity);
        this.autoUpdateListLength = autoUpdateListLength;
        this.eachListMaxLength = eachListMaxLength;

        table = new CacheNode[this.capacity];
        listsLength = new int[this.capacity];
        locks = new Object[this.capacity];
        Init();
    }

    private void Init(){

        for(int i=0;i<this.capacity;i++){
            table[i] = new CacheNode<V>("null",(V)new Object());
        }
        for(int i=0;i<this.capacity;i++){
            locks[i] = new Object();
        }
    }

    private int hashInTable(String key){
        int h;
        h = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        return h & capacity -1 ;
    }

    private void addFirst(CacheNode<V> node,int h){
        CacheNode<V> c = table[h];
        table[h] = node;
        if(node == null){
            return;
        }
        table[h].next = c;
    }

    private CacheNode<V> removeLast(int h){
        CacheNode<V> node = table[h];
        CacheNode<V> prevnode = null;
        if(node == null ){
            return null;
        }
        if(table[h].next == null ){
            table[h] = null;
        }
        while(node.next != null){
            prevnode = node;
            node = node.next;
        }

        //remove
        if(prevnode != null)
            prevnode.next = null;
        else{
            table[h] = new CacheNode<V>("null",(V)new Object());
        }

        return node;
    }

    public  CacheNode<V> put(String key,V value){
        int h = hashInTable(key);

        synchronized (locks[h]) {
            if(get(key) != null){
                table[h].value = value;
                return table[h];
            }
            addFirst(new CacheNode< V>(key, value), h);

            boolean overMaxLength = true;
            synchronized (sizeLock) {
                try {
                    overMaxLength = listsLength[h] > eachListMaxLength *
                            (!autoUpdateListLength ? 1 : ((listsLength[h] * 15 / (size+1)  +0.4)));
                    if (!overMaxLength) {
                        size++;
                    }
                }catch (Exception e){
                    System.out.println("" + size + this.autoUpdateListLength);
                    System.exit(0);
                }
            }
            if (overMaxLength) {  //动态调整
                return removeLast(h);
            } else {
                listsLength[h]++;
                return null;
            }
        }
    }

    public V get(String key){
        int h = hashInTable(key);
        synchronized (locks[h]) {
            CacheNode<V> currentnode = table[h];
            CacheNode<V> prevnode = null;
            V result = null;
            while (currentnode != null) {
                if (currentnode.key.equals(key)) {
                    result = currentnode.value;
                    break;
                }
                prevnode = currentnode;
                currentnode = currentnode.next;
            }

            //update the position of the node which is found.
            if(currentnode != null && !currentnode.key.equals(table[h].key)) {
                if(prevnode != null) {
                    prevnode.next = currentnode.next;
                }
                addFirst(currentnode, h);
            }

            return result;
        }
    }

    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= DEFAULT_MAX_CAPACITY) ? DEFAULT_MAX_CAPACITY : n + 1;
    }

    int getSize(){
        return size;
    }
}
