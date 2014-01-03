package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.Iterator;

public class SimSetting
{
  public String mStartDate;
  public double mStartBalance;
  public double mTradeAmount;
  public double mMaxLoss;
  public int mMaxStock;
  public int mMaxTradePerDay;
  public boolean mIsAllowDuplicate;
  public int mHeld;
  public double mProfitAdjustment = 1.0;

  public ArrayList mStrategyList = new ArrayList(8);

  public void clear()
  {
    mStartDate = null;
    mStartBalance = 0.0;
    mTradeAmount = 0.0;
    mMaxLoss = 0.0;
    mMaxStock = 0;
    mMaxTradePerDay = 0;
    mHeld = -1;

    Iterator iter = mStrategyList.iterator();
    while (iter.hasNext())
    {
      SimSettingStrategy strat = (SimSettingStrategy) iter.next();
      strat.clear();
    }
    mStrategyList.clear();
  }

  public String toString()
  {
    StringBuilder buffer = new StringBuilder(1024);
    buffer.append("StartDate: ").append(mStartDate).append("\n");
    buffer.append("StartBalance: ").append(mStartBalance).append("\n");
    buffer.append("TradeAmount: ").append(mTradeAmount).append("\n");
    buffer.append("MaxLoss: ").append(mMaxLoss).append("\n");
    buffer.append("MaxStock: ").append(mMaxStock).append("\n");
    buffer.append("MaxTradePerDay: ").append(mMaxTradePerDay).append("\n");
    buffer.append("IsAllowDuplicate: ").append(mIsAllowDuplicate).append("\n");
    buffer.append("Held: ").append(mHeld).append("\n");
    buffer.append("ProfitAdjustment: ").append(mProfitAdjustment).append("\n");
    buffer.append("===== StrategyList ===== ").append("\n");

    Iterator iter = mStrategyList.iterator();
    while (iter.hasNext())
      buffer.append(iter.next().toString()).append("\n");

    return buffer.toString();
  }
}
