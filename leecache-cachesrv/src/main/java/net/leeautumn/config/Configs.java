package net.leeautumn.config;

import net.leeautumn.constant.LoggerName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;

/**
 * Created by LeeAutumn on 11/25/16.
 * blog: leeautumn.net
 */
public class Configs {
    private static HashMap<Object,Object> configs = new HashMap<Object,Object>();
    private static Logger logger = LoggerFactory.getLogger(LoggerName.ConfigsLoggerName);

    static {
        new Configs();
    }

    public Configs(){
        String currentPath = this.getClass().getClassLoader().getResource(".").getPath();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(currentPath+"leecache-config.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The leecache-config paramters is:");
        for(Map.Entry entry : properties.entrySet()){
            configs.put(entry.getKey(),entry.getValue());
            logger.info("Config:{},{}",entry.getKey(),entry.getValue());
        }
    }

    public static String get(String key){
        return (String)configs.get(key);
    }
}
