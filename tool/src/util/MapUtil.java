/*---------------------------------------------------------------------------
| $Id: MapUtil.java,v 1.22 2012/06/02 09:33:21 quoc Exp $ 
| Copyright (c) 2000 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import com.qtt.tool.io.StreamUtil;

//===========================================================================
/**A whole bunch of static methods for easy retreival of primitive data type 
 * from a Map. */
//===========================================================================
public class MapUtil
{
  public static HashMap createHashMap(Object valuePair)
  {
    return createHashMap(valuePair, false);
  }

  public static HashMap createHashMap(Object valuePair, boolean isReverse)
  {
    return (HashMap) fillMap(new HashMap(), valuePair, isReverse);
  }

  public static Hashtable createHashtable(Object valuePair)
  {
    return createHashtable(valuePair, false);
  }

  public static Hashtable createHashtable(Object valuePair, boolean isReverse)
  {
    return (Hashtable) fillMap(new Hashtable(), valuePair, isReverse);
  }

  public static TreeMap createTreeMap(Object valuePair)
  {
    return createTreeMap(valuePair, false);
  }

  public static TreeMap createTreeMap(Object valuePair, boolean isReverse)
  {
    return (TreeMap) fillMap(new TreeMap(), valuePair, isReverse);
  }

  public static LinkedHashMap createLinkedHashMap(Object valuePair)
  {
    return createLinkedHashMap(valuePair, false);
  }

  public static LinkedHashMap createLinkedHashMap(Object valuePair,
                                                  boolean isReverse)
  {
    return (LinkedHashMap) fillMap(new LinkedHashMap(), valuePair, isReverse);
  }

  public static Map fillMap(Map table, Object valuePair, boolean isReverse)
  {
    int kIndex = 0;
    int vIndex = 0;
    Object[] valuePairArray = (Object[]) valuePair;
    for(int i=0; i<valuePairArray.length; i+=2)
    {
      if (isReverse)
      {
        kIndex = i + 1;
        vIndex = i;
      }
      else
      {
        kIndex = i;
        vIndex = i + 1;
      }

      table.put(valuePairArray[kIndex], valuePairArray[vIndex]);
    }
    return table;
  }

  public static void append(Map target, Map map)
  {
    Iterator iterator = map.keySet().iterator();
    while(iterator.hasNext())
    {
      Object key = iterator.next();
      target.put(key, map.get(key));
    }
  }

  public static void saveMap(Map map, String filename)
  {
    PrintWriter writer = StreamUtil.openPrintWriter(filename);
    Dumper.showMap(map, writer, "|"); 
    writer.close();
  }

  public static void loadMap(Map map, String filename)
  {
    BufferedReader reader = null;
    try
    {
      reader = StreamUtil.openBufferedReader(filename);
      String line = null;
      while( (line = reader.readLine()) != null)
      {
        int index = line.indexOf("|");
        if (index == -1)
          continue;
        String variable = line.substring(0, index);
        String value = line.substring(index+1);
        map.put(variable, value); 
      }
      reader.close();
    }
    catch(IOException e){ throw new RuntimeException(e); }
    finally{ StreamUtil.close(reader); }
  }

  public static void loadMapLong(Map map, String filename)
  {
    BufferedReader reader = null;
    try
    {
      reader = StreamUtil.openBufferedReader(filename);
      String line = null;
      while ( (line = reader.readLine()) != null)
      {
        String[] field = line.split("\\|");
        if (field.length != 2)
          continue;
        map.put(field[0].trim(), new Long(field[1].trim())); 
      }
      reader.close();
    }
    catch(IOException e){ throw new RuntimeException(e); }
    finally{ StreamUtil.close(reader); }
  }

  public static short getShortFromString(Map table, Object key)
  {
    return NumberUtil.toShort((String)table.get(key));
  }

  public static int getIntFromString(Map table, Object key)
  {
    return NumberUtil.toInt((String)table.get(key));
  }

  public static long getLongFromString(Map table, Object key)
  {
    return NumberUtil.toLong((String)table.get(key));
  }

  public static float getFloatFromString(Map table, Object key)
  {
    return NumberUtil.toFloat((String)table.get(key));
  }

  public static double getDoubleFromString(Map table, Object key)
  {
    return NumberUtil.toDouble((String)table.get(key));
  }

  public static boolean getBooleanFromString(Map table, Object key)
  {
    return NumberUtil.toBoolean((String)table.get(key));
  }

  //-------------------------------------------------------------------------
  /**Get an 'int' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @return An 'int' from the Map. Returns 0 if there is no matching
   *         key in the Map. */
  //-------------------------------------------------------------------------
  public static int getInt(Map table, Object key)
  {
    return getInt(table,key,0);
  }

  //-------------------------------------------------------------------------
  /**Get an 'int' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @param nDefault The default value to return if there is no matching key.
   * @return An 'int' from the Map. Returns 'nDefault' if there is no
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static int getInt(Map table, Object key, int nDefault)
  {
    Integer number=(Integer)table.get(key);
    if (number==null)
      return nDefault;

    return number.intValue();
  }

  public static void putInt(Map table, Object key, int nValue)
  {
    table.put(key, new Integer(nValue));
  }


  //-------------------------------------------------------------------------
  /**Get an 'short' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @return An 'short' from the Map. Returns 0 if there is no matching
   *         key in the Map. */
  //-------------------------------------------------------------------------
  public static short getShort(Map table, Object key)
  {
    return getShort(table,key,(short)0);
  }

  //-------------------------------------------------------------------------
  /**Get an 'short' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @param nDefault The default value to return if there is no matching key.
   * @return An 'short' from the Map. Returns 'nDefault' if there is no
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static short getShort(Map table, Object key, short nDefault)
  {
    Short number=(Short)table.get(key);
    if (number==null)
      return nDefault;
    return number.shortValue();
  }

  public static void putInt(Map table, Object key, short nValue)
  {
    table.put(key, new Short(nValue));
  }

  //-------------------------------------------------------------------------
  /**Get an 'long' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @return An 'long' from the Map. Returns 0 if there is no matching
   *         key in the Map. */
  //-------------------------------------------------------------------------
  public static long getLong(Map table, Object key)
  {
    return getLong(table,key,0L);
  }

  //-------------------------------------------------------------------------
  /**Get an 'long' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @param nDefault The default value to return if there is no matching key.
   * @return An 'long' from the Map. Returns 'nDefault' if there is no
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static long getLong(Map table, Object key, long nDefault)
  {
    Long number=(Long)table.get(key);
    if (number==null)
      return nDefault;
    return number.longValue();
  }

  public static void putLong(Map table, Object key, long nValue)
  {
    table.put(key, new Long(nValue));
  }

  public static boolean getBoolean(Map table, Object key)
  {
    return getBoolean(table, key, false);
  }

  public static boolean getBoolean(Map table, Object key, boolean defValue)
  {
    Boolean b = (Boolean) table.get(key);
    if (b == null)
      return defValue;
    return b.booleanValue();
  }

  //-------------------------------------------------------------------------
  /**Get an 'byte' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @return An 'byte' from the Map. Returns 0 if there is no matching
   *         key in the Map. */
  //-------------------------------------------------------------------------
  public static byte getByte(Map table, Object key)
  {
    return getByte(table,key,(byte)0);
  }

  //-------------------------------------------------------------------------
  /**Get an 'byte' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @param nDefault The default value to return if there is no matching key.
   * @return An 'byte' from the Map. Returns 'nDefault' if there is no
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static byte getByte(Map table, Object key, byte nDefault)
  {
    Byte number=(Byte)table.get(key);
    if (number==null)
      return nDefault;
    return number.byteValue();    
  }

  public static void putByte(Map table, Object key, byte nValue)
  {
    table.put(key, new Byte(nValue));
  }

  //-------------------------------------------------------------------------
  /**Get an 'char' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @return An 'char' from the Map. Returns '0' if there is no 
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static char getChar(Map table, Object key)
  {
    return getChar(table,key,'0');
  }

  //-------------------------------------------------------------------------
  /**Get an 'char' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @param nDefault The default value to return if there is no matching key.
   * @return An 'char' from the Map. Returns 'nDefault' if there is no
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static char getChar(Map table, Object key, char nDefault)
  {
    Character number=(Character)table.get(key);
    if (number==null)
      return nDefault;
    return number.charValue();    
  }

  public static void putChar(Map table, Object key, char cValue)
  {
    table.put(key, new Character(cValue));
  }

  //-------------------------------------------------------------------------
  /**Get an 'float' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @return An 'float' from the Map. Returns 0.0 if there is no 
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static float getFloat(Map table, Object key)
  {
    return getFloat(table,key,0.0f);
  }

  //-------------------------------------------------------------------------
  /**Get an 'float' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @param nDefault The default value to return if there is no matching key.
   * @return An 'float' from the Map. Returns 'nDefault' if there is no
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static float getFloat(Map table, Object key, float nDefault)
  {
    Float number=(Float)table.get(key);
    if (number==null)
      return nDefault;
    return number.floatValue();    
  }

  public static void putFloat(Map table, Object key, float fValue)
  {
    table.put(key, new Float(fValue));
  }

  //-------------------------------------------------------------------------
  /**Get an 'double' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @return An 'double' from the Map. Returns 0.0 if there is no 
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static double getDouble(Map table, Object key)
  {
    return getDouble(table,key,0.0);
  }

  //-------------------------------------------------------------------------
  /**Get an 'double' value from the Map.
   * @param table The hash table.
   * @param key The key for retreiving the value in the Map.
   * @param nDefault The default value to return if there is no matching key.
   * @return An 'double' from the Map. Returns 'nDefault' if there is no
   *         matching key in the Map. */
  //-------------------------------------------------------------------------
  public static double getDouble(Map table, Object key, double nDefault)
  {
    Double number=(Double)table.get(key);
    if (number==null)
      return nDefault;
    return number.doubleValue();    
  }  

  public static void putDouble(Map table, Object key, double fValue)
  {
    table.put(key, new Double(fValue));
  }

  public static Vector keyToVector(Map table)
  {
    Vector vector = new Vector();

    Set s=table.keySet();
    Iterator it=s.iterator();
    while (it.hasNext())
    {
      vector.add(it.next());
    }    

    return vector;
  }

  public static Object get(Map table, Object key, Object defaultValue)
  {
    Object value = table.get(key);
    return (value == null) ? defaultValue : value;
  }

  public static boolean isSameItem(Map table1, Map table2, Object key)
  {
    Object item1 = table1.get(key);
    if (item1 == null)
      return false;

    Object item2 = table2.get(key);
    if (item2 == null)
      return false;

    return item1.equals(item2);
  }
}
