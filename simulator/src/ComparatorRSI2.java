package com.qtt.app.stock.simulator;

import java.util.Comparator;
import java.util.Map;
import com.qtt.tool.util.MapUtil;

public class ComparatorRSI2 implements Comparator
{
  public int compare(Object o1, Object o2)
  {
    return (int) (getDouble(o1)*100.0 - getDouble(o2)*100.0);
  }

  public boolean equals(Object o1)
  {
    return compare(this, 01) == 0;
  }

  double getDouble(Object data)
  {
    Map signalData = (Map) data;
    if (! (signalData.get("rsi2") instanceof Double))
      return 0.0;

    return MapUtil.getDouble(signalData, "rsi2");
  }
}
