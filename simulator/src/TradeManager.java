package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.qtt.tool.util.MapUtil;
import static com.qtt.db.common.SignalFieldConstant.CREATE_DATE;

public class TradeManager
{
  SimSetting mSimSetting;
  SimSettingStrategy mSimStrat;
  String mAction;
  String mActionClose;
  ArrayList mExitSetting = new ArrayList(8);

  Trade mTrade;
  boolean mIsDone = false;
  int mDay = 0;

  public TradeManager(SimSetting simSetting, SimSettingStrategy simStrat, 
                      Map signalInfo)
  {
    mAction = (String) signalInfo.get("action");
    mSimSetting = simSetting;
    mSimStrat = simStrat;

    if (mAction.equals("buy"))
    {
      mActionClose = "sell";
    }
    else
    {
      mActionClose = "cover";
    }

    setupExitSetting();

    setupTrade(signalInfo, mAction, null);
  }

  public boolean isDone(){ return mIsDone; }

  public void input(ArrayList signalData)
  {
    if (mExitSetting == null)
      return;

    Map signal = getSignal(signalData);
    mDay++;
    if (signal == null)
    {
      String date = "";
      if (signalData.size() > 0)
      {
        Map s = (Map) signalData.get(0);
        date = (String) s.get(CREATE_DATE);
      }

      Util.mLogger.error("Can't find signal for id: {}-{}", mTrade.mStockId,
                         date);
      return;
    }

    int size = mExitSetting.size();
    for (int i=0; i<size; i++)
    {
      ArrayList conditionList = (ArrayList) mExitSetting.get(i);
      if (mSimSetting.mHeld > 0 && mDay >= mSimSetting.mHeld ||
          Trigger.isMatch(signal, conditionList))
      {
        setupTrade(signal, mActionClose, mTrade);
        mIsDone = true;
        break;
      }
    }
  }

  Map getSignal(ArrayList signalData)
  {
    int size = signalData.size();
    for (int i=0; i<size; i++)
    {
      Map signal = (Map) signalData.get(i);
      int id = (int) MapUtil.getInt(signal, "id");
      if (id == mTrade.mStockId)
        return signal;
    }

    return null;
  }

  void setupTrade(Map signalInfo, String action, Trade trade)
  {
    mTrade = new Trade();
    mTrade.mStockId = (int) MapUtil.getInt(signalInfo, "id");
    mTrade.mDate = (String) signalInfo.get("create_date");
    mTrade.mHeld = mDay;

    if (action.equals("buy"))
      mTrade.mTransactionType = Trade.TRANS_BUY;
    else if (action.equals("short"))
      mTrade.mTransactionType = Trade.TRANS_SHORT;
    else if (action.equals("sell"))
      mTrade.mTransactionType = Trade.TRANS_SELL;
    else if (action.equals("cover"))
      mTrade.mTransactionType = Trade.TRANS_COVER;

    mTrade.mPrice = MapUtil.getDouble(signalInfo, "price");
    mTrade.mRSI = MapUtil.getDouble(signalInfo, "rsi2");
    double tradeAmount = mSimSetting.mTradeAmount;

    if (trade == null)
    {
      mTrade.mQuantity = (int) (tradeAmount / mTrade.mPrice);
      if (mTrade.mQuantity <= 0)
        mTrade.mQuantity = 1;
    }
    else
    {
      mTrade.mQuantity = trade.mQuantity;
      mTrade.mMatch = trade;
    }

    Transaction.instance().addTrade(mTrade);
  }

  void setupExitSetting()
  {
    mExitSetting.clear();
    Iterator iter = mSimStrat.mExitTriggerList.iterator();
    while (iter.hasNext())
    {
      TriggerData triggerData = (TriggerData) iter.next();
      if (mAction.equals("buy"))
      {
        if (triggerData.mAction.equals("sell"))
          mExitSetting.add(triggerData.mConditionList);
      }
      else if (mAction.equals("short"))
      {
        if (triggerData.mAction.equals("cover"))
          mExitSetting.add(triggerData.mConditionList);
      }
      else
      {
        Util.mLogger.error("TradeManager.mAction is not buy or sell!");
      }
    }
  }
}
