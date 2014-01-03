/*---------------------------------------------------------------------------
| $Id: ComparatorRDouble.java,v 1.1 2014/01/01 17:21:08 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.app.stock.simulator;

import java.util.Comparator;
import java.util.Map;

public class ComparatorRDouble implements Comparator
{
  String m_field;

  public ComparatorRDouble(){};
  public ComparatorRDouble(String fieldName){ setField(fieldName); }

  public int compare(Object o1, Object o2)
  {
    return (int) ((getNumber(o1) - getNumber(o2)) * 1000);
  }

  private double getNumber(Object data)
  {
    if (data == null)
      return 0.0;

    Double number = (Double) ((Map) data).get(m_field);
    if (number == null)
      return 0.0;

    return number.doubleValue();
  }

  public boolean equals(Object o1)
  { 
    return getNumber(o1) == getNumber(this);
  }

  public void setField(String field){ m_field = field; }
}
