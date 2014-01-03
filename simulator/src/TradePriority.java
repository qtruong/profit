package com.qtt.app.stock.simulator;

import java.util.ArrayList;

public class TradePriority
{
  public final static int TYPE_BUY = 1;
  public final static int TYPE_SHORT = 2;
  public final static int ORDER_ASC = 1;
  public final static int ORDER_DESC = 2;
  public final static int ORDER_MIDDLE = 3;

  public int mType;
  public String mField;
  public int mOrder;
  public ArrayList mPrioritySymbolList;

  public String toString()
  {
    StringBuilder buffer = new StringBuilder(128);
    buffer.append("Type: ").append(mType).append("\n");
    buffer.append("Field: ").append(mField).append("\n");
    buffer.append("Order: ").append(mOrder).append("\n");
    buffer.append("PrioritySymbolList: ");
    if (mPrioritySymbolList == null)
    {
      buffer.append("<empty>");
    }
    else
    {
      int size = mPrioritySymbolList.size();
      for (int i=0; i<size; i++)
        buffer.append(" ").append(mPrioritySymbolList.get(i));
    }
    buffer.append("\n");

    return buffer.toString();
  }
}
