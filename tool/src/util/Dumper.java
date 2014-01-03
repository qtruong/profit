/*---------------------------------------------------------------------------
| $Id: Dumper.java,v 1.16 2014/01/01 15:03:21 quoc Exp $
| Copyright (c) 2002 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Dumper
{
  public static final String NEW_LINE = System.getProperty("line.separator");

  static boolean m_isShowArrayIndex = false;

  public static void setShowArrayIndex(boolean show)
  {
    m_isShowArrayIndex = show;
  }

  public static boolean getShowArrayIndex(){ return m_isShowArrayIndex; }

  public static String showAsString(Object data)
  {
    StringWriter swriter = new StringWriter(64);
    show(data, new PrintWriter(swriter));
    return swriter.toString();
  }

  public static void showDimension(Component c)
  {
    showDimension(c, null);
  }
  public static void showDimension(Component c, PrintWriter writer)
  {
    boolean needFlush = false;

    if (writer == null)
    {
      writer = new PrintWriter(System.out);
      needFlush = true;
    }

    writer.print("min: ");
    show(c.getMinimumSize(), writer);
    writer.print("max: ");
    show(c.getMaximumSize(), writer);
    writer.print("prefer: ");
    show(c.getPreferredSize(), writer);
    writer.print("size: ");
    show(c.getSize(), writer);

    if (needFlush)
      writer.flush();
  }

  //-------------------------------------------------------------------------
  /**Dump an object's data to the screen.
   *
   * @param data The data to be dumped to the screen. */
  //-------------------------------------------------------------------------
  public static void show(Object data)
  {
    show(data, null);
  }

  public static void show(Object data, PrintWriter writer)
  {
    show(data, writer, true);
  }

  public static void show(Object data, PrintWriter writer, boolean isNewLine)
  {
    show(data, writer, true, 0);
  }

  public static void show(Object data, PrintWriter writer, boolean isNewLine,
                          int indent)
  {
    show(data, writer, true, indent, indent);
  }

  //-------------------------------------------------------------------------
  /**Dump an object's data to a PrintWriter.
   *
   * @param data The data to be dumped to the screen. 
   * @param writer The data will be output to this PrintWriter. */
  //-------------------------------------------------------------------------
  public static void show(Object data, PrintWriter writer, boolean isNewLine,
                          int indent, int indentValue)
  {
    boolean isUsingStandardOut = false;
    if (writer == null)
    {
      writer = new PrintWriter(System.out);
      isUsingStandardOut = true;
    }

    if (data == null)
    {
      print("(null)", writer, isNewLine, indent);
      return;
    }

    Class dataClass = data.getClass();
    if (dataClass.isArray())
    {
      writer.println();
      printIndent(indent, writer);
      writer.println("[");
      Class componentType = dataClass.getComponentType();
      indent += 2;
      if (componentType.isPrimitive())
      {
        if (componentType == byte.class)
          showByteArray((byte[])data, writer, isNewLine, indent);
        else if (componentType == short.class)
          showShortArray((short[])data, writer, isNewLine, indent);
        else if (componentType == int.class)
          showIntArray((int[])data, writer, isNewLine, indent);
        else if (componentType == long.class)
          showLongArray((long[])data, writer, isNewLine, indent);
        else if (componentType == float.class)
          showFloatArray((float[])data, writer, isNewLine, indent);
        else if (componentType == double.class)
          showDoubleArray((double[])data, writer, isNewLine, indent);
      }
      else
      {
        showObjectArray((Object[])data, writer, isNewLine, indent);
      }
      indent -= 2;
      printIndent(indent, writer);
      writer.println("]");
    }
    else
    {
      if (data instanceof Map)
        showMap((Map)data, writer, " => ", indent);
      else if (data instanceof Collection)
        showCollection((Collection) data, writer, isNewLine, indent);
      else
        print(data, writer, isNewLine, indentValue);
    }

    if (!isNewLine)
      writer.println();

    if (isUsingStandardOut)
      writer.flush();
  }

  private static void showCollection(Collection collection, PrintWriter writer,
                                     boolean isNewLine, int indent)
  {
    int i = 0;
    Iterator iterator = collection.iterator();

    printIndent(indent, writer);
    writer.println("[");
    indent += 2;
    while(iterator.hasNext())
    {
      if (m_isShowArrayIndex)
      {
        writer.print("  " + i + ":");
        i++;
      }
      show(iterator.next(), writer, isNewLine, indent);
    }
    indent -= 2;
    printIndent(indent, writer);
    writer.println("]");
  }

  public static void showMap(Map table, PrintWriter writer, String separator)
  {
    showMap(table, writer, separator, 0);
  }

  public static void showMap(Map table, PrintWriter writer, String separator,
                             int indent)
  {
    printIndent(indent, writer);
    writer.println("{");
    indent += 2;
    Set s=table.keySet();
    Iterator it=s.iterator();
    while (it.hasNext())
    {
      Object obj=it.next();
      Object value=table.get(obj);
      print(obj+ separator, writer, null, indent);
      show(value, writer, true, indent, 0);
    }    
    indent -= 2;
    printIndent(indent, writer);
    writer.println("}");
  }

  private static void showObjectArray(Object[] data, PrintWriter writer, 
                                      boolean isNewLine, int indent)
  {
    for(int i=0; i<data.length; i++)
    {
      if (m_isShowArrayIndex)
        writer.print("  " + i + ":");
      show(data[i], writer, isNewLine, indent);
    }
  }

  private static void showByteArray(byte[] data, PrintWriter writer, 
                                    boolean isNewLine, int indent)
  {
    for(int i=0; i<data.length; i++)
    {
      if (m_isShowArrayIndex)
        writer.print("  " + i + ":");    
      show(new Byte(data[i]), writer, isNewLine, indent);
    }
  }

  private static void showShortArray(short[] data, PrintWriter writer, 
                                     boolean isNewLine, int indent)
  {
    for(int i=0; i<data.length; i++)
    {
      if (m_isShowArrayIndex)
        writer.print("  " + i + ":");    
      show(new Short(data[i]), writer, isNewLine, indent);
    }
  }

  private static void showIntArray(int[] data, PrintWriter writer, 
                                   boolean isNewLine, int indent)
  {
    for(int i=0; i<data.length; i++)
    {
      if (m_isShowArrayIndex)
        writer.print("  " + i + ":");    
      show(new Integer(data[i]), writer, isNewLine, indent);
    }
  }

  private static void showLongArray(long[] data, PrintWriter writer, 
                                    boolean isNewLine, int indent)
  {
    for(int i=0; i<data.length; i++)
    {
      if (m_isShowArrayIndex)
        writer.print("  " + i + ":");    
      show(new Long(data[i]), writer, isNewLine, indent);
    }
  }

  private static void showFloatArray(float[] data, PrintWriter writer, 
                                     boolean isNewLine, int indent)
  {
    for(int i=0; i<data.length; i++)
    {
      if (m_isShowArrayIndex)
        writer.print("  " + i + ":");    
      show(new Float(data[i]), writer, isNewLine, indent);
    }
  }

  private static void showDoubleArray(double[] data, PrintWriter writer, 
                                      boolean isNewLine, int indent)
  {
    for(int i=0; i<data.length; i++)
    {
      if (m_isShowArrayIndex)
        writer.print("  " + i + ":");    
      show(new Double(data[i]), writer, isNewLine, indent);
    }
  }

  private static void print(Object data, PrintWriter writer)
  {
    print(data, writer, true, 0);
  }

  private static void print(Object data, PrintWriter writer, boolean isNewLine,
                            int indent)
  {
    if (isNewLine)
      print(data, writer, NEW_LINE, indent);
    else
      print(data, writer, ", ", indent);
  }

  private static void print(Object data, PrintWriter writer, String suffix,
                            int indent)
  {
    printIndent(indent, writer);
    writer.print(data);
    if (suffix != null)
      writer.print(suffix);
  }

  private static void printIndent(int indent, PrintWriter writer)
  {
    for(int i=0; i<indent; i++)
      writer.print(" ");
  }
}
