package com.qtt.tool.util;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Performance 
{
  final static int BEGIN = 0;
  final static int END = 1;

  LinkedHashMap m_data = new LinkedHashMap();

  public void begin(String name)
  {
    double[] t = getData(name);
    t[BEGIN] = System.currentTimeMillis();
  }

  public void end(String name)
  {
    double[] t = getData(name); 
    t[END] = System.currentTimeMillis();
  }

  public void showResult()
  {
    showResult(new PrintWriter(System.out));
  }

  public void showResult(PrintWriter writer)
  {
    Iterator iter = m_data.keySet().iterator();
    while (iter.hasNext())
    {
      String name = (String) iter.next();
      double[] t = (double[]) m_data.get(name);
      writer.println("-------- " + name + " -----------");
      writer.println("Begin: " + t[BEGIN]);
      writer.println("End: " + t[END]);
      double elapse = t[END] - t[BEGIN];
      writer.print("Elapse: " + elapse + " ms");
      if (elapse > 1000)
      {
        double second = elapse / 1000;
        writer.print(" (" + second + " sec)");
        if (second > 60)
        {
          double min = second / 60;
          writer.println(" (" + min + " min)");
        }
      }
      writer.println();
    }
  }

  double[] getData(String name)
  {
    double[] t = (double[]) m_data.get(name);
    if (t == null)
    {
      t = new double[2];
      m_data.put(name, t);
    }

    return t;
  }
}
