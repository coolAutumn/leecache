package net.leeautumn.serialize;

/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public interface Serializer {
    Object parse(String o);

    String toString(Object o,Class c);
}
