package com.qtt.app.stock.simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.qtt.tool.util.MapUtil;
import com.qtt.tool.util.XMLUtil;
import static com.qtt.app.stock.simulator.Simulator.*;

public class Trader
{

  final static String[] SIGNAL_SETTING_FIELD = 
  {
    "name", "interval", "filter", "consecutive_day", "limit"
  };

  ArrayList<TradeManager> mTradeManagerList = new ArrayList<TradeManager>(32);
  ArrayList<Trigger> mTriggerList = new ArrayList<Trigger>(8);
  SimSetting mSimSetting;

  ArrayList<TradeManager> mRemoveList = new ArrayList<TradeManager>(64);

  public Trader(SimSetting simSetting)
  {
    mSimSetting = simSetting;
    setupTrigger();
  }

  public void input(ArrayList signalData)
  {
    // Wait for a signal to trigger a trade.
    // Once a trade is trigger, it'll be handle
    // by TradeManager until trade is close.
    if (mTradeManagerList.size() < mSimSetting.mMaxStock)
    { 
      int tradeCount = 0;
      for (Trigger trigger : mTriggerList)
      {
        if (trigger.isTrigger(signalData))
        {
          ArrayList signalInfoList = trigger.getSignalInfoList();
          if (signalInfoList == null || signalInfoList.size() == 0)
            break;

          orderSignalList(signalInfoList, trigger.getSimSettingStrategy());
          int size = signalInfoList.size();
          for (int i=0; i<size; i++)
          {
            Map signalInfo = (Map) signalInfoList.get(i);
            TradeManager tm = new TradeManager(mSimSetting,
                                             trigger.getSimSettingStrategy(),
                                             signalInfo);                                             
            mTradeManagerList.add(tm);
            ++tradeCount;
            if (isOverMaxTrade(tradeCount))
              break;
          }
        }

        if (isOverMaxTrade(tradeCount))
          break;
      }
    }

    mRemoveList.clear();
    for (TradeManager tradeManager : mTradeManagerList)
    {
      tradeManager.input(signalData);
      if (tradeManager.isDone())
        mRemoveList.add(tradeManager);
    }

    for (TradeManager tradeManager : mRemoveList)
      mTradeManagerList.remove(tradeManager);
  }

  boolean isOverMaxTrade(int tradeCount)
  {
    return tradeCount >= mSimSetting.mMaxTradePerDay || 
           mTradeManagerList.size() >= mSimSetting.mMaxStock;
  }

  void orderSignalList(ArrayList signalList, SimSettingStrategy strat)
  {
    Map signalInfo = (Map) signalList.get(0);
    String action = (String) signalInfo.get("action");
    if (action == null)
      return;

    TradePriority priority = null;
    if (action.equals("buy"))
      priority = strat.mBuyPriority;
    else if (action.equals("short"))
      priority = strat.mShortPriority;

    if (priority == null)
    {
      Util.mLogger.warn("priority == null");
      return;
    }

    Comparator comparator = new ComparatorRDouble(priority.mField);
    if (priority.mOrder == TradePriority.ORDER_DESC)
      comparator = Collections.reverseOrder(comparator);

    Collections.sort(signalList, comparator);

    // PrioritySymbol gets the highest priority.
    if (priority.mPrioritySymbolList == null)
      return;

    ArrayList priorityList = new ArrayList(8);
    Iterator iter = priority.mPrioritySymbolList.iterator();
    while (iter.hasNext())
    {
      String symbol = (String) iter.next();
      String idStr = Mapper.symbol2Id(symbol);
      if (idStr == null)
       continue; 

      int id = Integer.parseInt(idStr); 
      int index = findId(signalList, id);
      if (index < 0)
        continue;

      Map signal = (Map) signalList.remove(index);
      priorityList.add(signal);
    }
    signalList.addAll(0, priorityList);
  }

  public static void dumpSignalId(ArrayList signalList)
  {
    int signalListSize = signalList.size();
    for (int i=0; i<signalListSize; i++)
    {
      Map signal = (Map) signalList.get(i);
      Integer signalId = (Integer) signal.get("id");
      System.out.print(signalId + " ");
    }
    System.out.println();
  }

  int findId(ArrayList signalList, int id)
  {
    int size = signalList.size();
    for (int i=0; i<size; i++)
    {
      Map signal = (Map) signalList.get(i);
      Integer signalId = (Integer) signal.get("id");
      if (signalId.intValue() == id)
        return i;
    }

    return -1;
  }

  public void show(){}

  void setupTrigger()
  {
    Iterator iter = mSimSetting.mStrategyList.iterator();
    while (iter.hasNext())
    {
      SimSettingStrategy strat = (SimSettingStrategy) iter.next();
      mTriggerList.add(new Trigger(strat, "entry"));
    }
  }
}
