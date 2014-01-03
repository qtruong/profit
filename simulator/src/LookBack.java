package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class LookBack
{
  final static int LOOK_BACK_DAY = 10;

  LinkedList<Map> mLookBackList = new LinkedList<Map>();
  static LookBack mLookBack;

  public static LookBack instance()
  {
    if (mLookBack == null)
      mLookBack = new LookBack();

    return mLookBack;
  }

  private LookBack(){}

  public void add(ArrayList signalData)
  {
    if (mLookBackList.size() >= LOOK_BACK_DAY)
      mLookBackList.removeLast();

    Map signalMap = new HashMap();
    Iterator iter = signalData.iterator();
    while (iter.hasNext())
    {
      Map signal = (Map) iter.next();
      Object id = signal.get("id");
      if (id == null)
      {
        Util.mLogger.error("[LookBack.add] id == null");
        continue;
      }

      signalMap.put(id, signal);
    }

    mLookBackList.addFirst(signalMap);
  }

  public Map get(int day)
  {
    if (day >= 0)
      return null;

    int index = Math.abs(day) - 1;
    if (index >= mLookBackList.size())
      return null;

    return mLookBackList.get(index);
  }
}
