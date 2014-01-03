package com.qtt.app.stock.simulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class Mapper
{
  final static String MAPPER_KEY = "db.fs.mapper";

  static Map mSymbol2Id = new Hashtable();
  static Map mId2Symbol = new Hashtable();

  static
  {
    String configFile = System.getProperty("db.conf");
    Util.mLogger.info("db.conf: [{}]", configFile);
    if (configFile != null)
    {
      try
      {
        Properties prop = new Properties();
        prop.load(new FileInputStream(configFile));

        String mapperFile = prop.getProperty(MAPPER_KEY);
        Util.mLogger.info("{}: [{}]", MAPPER_KEY, mapperFile);

        Properties mapProp = new Properties();
        mapProp.load(new FileInputStream(mapperFile));
        mSymbol2Id.putAll(mapProp);

        Iterator iter = mSymbol2Id.keySet().iterator();
        while (iter.hasNext())
        {
          Object key = iter.next();
          mId2Symbol.put(mSymbol2Id.get(key), key);
        }
      }
      catch(IOException e)
      {
        throw new RuntimeException(e);
      }
    }
  };

  public static String symbol2Id(String symbol)
  { 
    return (String) mSymbol2Id.get(symbol.toUpperCase());
  }

  public static String id2Symbol(CharSequence id)
  { 
    return (String) mId2Symbol.get(id);
  }
}
