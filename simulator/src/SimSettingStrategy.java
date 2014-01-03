package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.Iterator;

public class SimSettingStrategy
{
  public String mName;
  public ArrayList mSymbolList = new ArrayList(16);
  public TradePriority mBuyPriority = new TradePriority();
  public TradePriority mShortPriority = new TradePriority();
  public ArrayList mEntryTriggerList = new ArrayList(16);
  public ArrayList mExitTriggerList = new ArrayList(16);

  public void clear()
  {
    mSymbolList.clear();
  }

  public String toString()
  {
    StringBuilder buffer = new StringBuilder(1024);
    buffer.append("Name: ").append(mName).append("\n");
    buffer.append("SymbolList: ");
    if (mSymbolList == null)
    {
      buffer.append("<empty>");
    }
    else
    {
      int size = mSymbolList.size();
      for (int i=0; i<size; i++)
        buffer.append(" ").append(mSymbolList.get(i));
    }
    buffer.append("\n");
    buffer.append("====BuyPriority====").append("\n");
    buffer.append(mBuyPriority.toString());
    buffer.append("====ShortPriority====").append("\n");
    buffer.append(mShortPriority.toString());

    buffer.append("======EntryTriggerList====").append("\n");
    appendTriggerList(buffer, mEntryTriggerList);
    buffer.append("======ExitTriggerList====").append("\n");
    appendTriggerList(buffer, mExitTriggerList);

    return buffer.toString();
  }

  void appendTriggerList(StringBuilder buffer, ArrayList triggerList)
  {
    if (triggerList == null)
      return;

    Iterator iter = triggerList.iterator();
    while (iter.hasNext())
    {
      TriggerData triggerData = (TriggerData) iter.next();
      buffer.append(triggerData.toString());
    }
  }

  void appendConditionList(StringBuilder buffer, ArrayList conditionList)
  {
    if (conditionList == null)
      return;

    Iterator iter = conditionList.iterator();
    while (iter.hasNext())
    {
      TriggerCondition condition = (TriggerCondition) iter.next();
      buffer.append(condition.toString());
    }
  }
}
