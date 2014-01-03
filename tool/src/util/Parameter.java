/*---------------------------------------------------------------------------
| $Id: Parameter.java,v 1.7 2012/06/02 09:33:21 quoc Exp $ 
| Copyright (c) 2000 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

public class Parameter
{
  public Class[] type;
  public Object[] data;
  public String[] variable;

  public Parameter(int nSize)
  {
    type=new Class[nSize];
    data=new Object[nSize];
    variable=new String[nSize];
  }

  public boolean equals(Object n)
  {
    Parameter param=(Parameter)n;

    if (type.length!=param.type.length)
    {
      System.out.println("Different size");
      return false;
    }

    for(int i=0; i<type.length; i++)
    {
      if (!same(type[i],param.type[i]))
      {
        System.out.println("type not equal");
        return false;
      }

      if (!same(data[i],param.data[i]))
      {
        System.out.println("data not equal");
        return false;
      }

      if (!same(variable[i],param.variable[i]))
      {
        System.out.println("varaible not equal");
        return false;
      }
    }

    return true;
  }

  private boolean same(Object data1, Object data2)
  {
    if (data1==null)
    {
      if (data2==null)
        return true;
      else
        return false;
    }
    
    if (data2==null)
      return false;

    return data1.equals(data2);
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer(64);
    for(int i=0; i<type.length; i++)
    {
      buffer.append("type: ").append(type[i]).append("\n");
      buffer.append("data: ").append(data[i]).append("\n");
      buffer.append("variable: ").append(variable[i]).append("\n");
    }
    return buffer.toString();
  }
}
