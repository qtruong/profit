package com.qtt.app.stock.simulator;

import com.qtt.tool.util.NumberUtil;

public class Trade
{
  public final static int TRANS_BUY = 1;
  public final static int TRANS_SELL = 2;
  public final static int TRANS_SHORT = 3;
  public final static int TRANS_COVER = 4;

  public int mStockId;
  public int mQuantity;
  public String mDate;
  public double mPrice;
  public int mTransactionType;
  public Trade mMatch;
  public int mHeld;
  public double mRSI;

  public String toString()
  {
    double profit = getProfit();

    return mDate + "|" + mStockId + "|" + mQuantity + "|" + mPrice + "|" +
           mTransactionType + "|" + NumberUtil.toDecimalFormat(profit);
  }

  public double getProfit()
  {
    if (mMatch != null)
      return getAmount(this) + getAmount(mMatch);

    return 0.0;
  }

  public boolean isClose()
  {
    return mTransactionType == TRANS_SELL || mTransactionType == TRANS_COVER;
  }

  double getAmount(Trade trade)
  {
    double modifier = 1.0;
    if (trade.mTransactionType == TRANS_BUY ||
        trade.mTransactionType == TRANS_COVER)
    {
      modifier = -1.0;
    }

    return trade.mPrice * (double) trade.mQuantity * modifier; 
  }
}
