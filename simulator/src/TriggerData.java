package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.Iterator;

public class TriggerData
{
  public String mAction; // buy, short, sell, cover
  public ArrayList mConditionList = new ArrayList(16);

  public String toString()
  {
    StringBuilder buffer = new StringBuilder(1024);
    buffer.append("==TriggerData==\n");
    buffer.append("Action: ").append(mAction).append("\n");
    buffer.append("====ConditionList===\n");

    Iterator iter = mConditionList.iterator();
    while (iter.hasNext())
    {
      TriggerCondition condition = (TriggerCondition) iter.next(); 
      buffer.append(condition.toString());
    }
    return buffer.toString();
  }
}
