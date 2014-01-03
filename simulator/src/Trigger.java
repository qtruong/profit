package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.qtt.tool.util.MapUtil;
import static com.qtt.app.stock.simulator.Simulator.*;

public class Trigger
{
  SimSettingStrategy mStrategySetting;
  ArrayList mTriggerList;
  ArrayList mSignalInfoList = new ArrayList(16);
  String mAction;

  // Contains list of symbols that will be triggered. 
  Map mStockIdTable = new HashMap();

  public Trigger(SimSettingStrategy strategySetting, String triggerType)
  {
    if (strategySetting == null || triggerType == null)
    {
      Util.mLogger.error("setting={} triggerType={}", strategySetting,
                         triggerType);
      return;
    }

    mStrategySetting = strategySetting;
    if (triggerType.equals("entry"))
      mTriggerList = mStrategySetting.mEntryTriggerList;
    else
      mTriggerList = mStrategySetting.mExitTriggerList;
      
    setupStockIdTable();
  }

  public String getAction(){ return mAction; }
  public SimSettingStrategy getSimSettingStrategy(){ return mStrategySetting; }
  public ArrayList getSignalInfoList(){ return mSignalInfoList; }

  void setupStockIdTable()
  {
    mStockIdTable.clear();
    if (mStrategySetting.mSymbolList == null)
      return;

    Iterator iter = mStrategySetting.mSymbolList.iterator();
    while (iter.hasNext())
    {
      String symbol = (String) iter.next();
      String id = Mapper.symbol2Id(symbol);
      mStockIdTable.put(Integer.parseInt(id), symbol);
    }
  }

  public boolean isTrigger(ArrayList signalData)
  {
    mSignalInfoList.clear();

    Iterator iter = mTriggerList.iterator();
    while (iter.hasNext())
    {
      TriggerData triggerData = (TriggerData) iter.next();
      mAction = triggerData.mAction;

      ArrayList matchList = getMatch(signalData, triggerData.mConditionList,
                                     mAction);
      if (matchList != null)
        mSignalInfoList.addAll(matchList);
    }
    
    return mSignalInfoList.size() > 0;
  }

  ArrayList getMatch(ArrayList signalData, ArrayList conditionList,
                     String action)
  {
    ArrayList matchList = new ArrayList(8);
    int size = signalData.size();
    for (int i=0; i<size; i++)
    {
      Map signal = (Map) signalData.get(i);
      int id = MapUtil.getInt(signal, "id");
      if (!mStockIdTable.containsKey(id))
        continue;

      if (isMatch(signal, conditionList))
      {
        signal.put("action", action);
        matchList.add(signal);
      }
    }

    return matchList;
  }

  Map getMatchOld(ArrayList signalData, ArrayList conditionList)
  {
    int size = signalData.size();
    for (int i=0; i<size; i++)
    {
      Map signal = (Map) signalData.get(i);
      int id = MapUtil.getInt(signal, "id");
      if (!mStockIdTable.containsKey(id))
        continue;

      if (isMatch(signal, conditionList))
        return signal;
    }

    return null;
  }

  public static boolean isMatch(Map signal, ArrayList conditionList)
  {
    int size = conditionList.size();
    for (int i=0; i<size; i++)
    {
      TriggerCondition condition = (TriggerCondition) conditionList.get(i);
      if (!condition.isConditionTrue(signal))
        return false;
    }

    return true;
  }
}
