package net.leeautumn.selectorhandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Created by LeeAutumn on 11/25/16.
 * blog: leeautumn.net
 */
public abstract class Handler implements Runnable{
    public Selector readSelector = null;
    public abstract void handleRequest(SelectionKey key);
    public void setReadSelector(Selector selector){
        this.readSelector = selector;
    }
}
