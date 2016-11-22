package net.leeautumn.serialize.json;

import com.alibaba.fastjson.JSON;
import net.leeautumn.serialize.Serializer;


/**
 * Created by LeeAutumn on 11/22/16.
 * blog: leeautumn.net
 */
public class JSONSerializer implements Serializer {

    public Object parse(String o) {
        return JSON.parse(o);
    }


    public String toString(Object o, Class c) {
        return JSON.toJSONString(o);
    }
}
