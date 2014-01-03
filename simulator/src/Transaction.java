package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import com.qtt.tool.util.NumberUtil;

/*-----------------------------------------------------------------------
 * Singleton that is used to record and display all transactions
-----------------------------------------------------------------------*/ 
public class Transaction
{
  ArrayList<Trade> tradeList = new ArrayList<Trade>(256);
  SimSetting mSimSetting;

  static Transaction mTransaction;

  public static Transaction instance()
  {
    if (mTransaction == null)
      mTransaction = new Transaction();

    return mTransaction;
  }

  private Transaction(){}

  public void setSimSetting(SimSetting setting){ mSimSetting = setting; }

  public void addTrade(Trade trade)
  {
    tradeList.add(trade);
  }

  public void showAll()
  {
    System.out.println("=========== Transaction results =============");
    if (tradeList.size() == 0)
    {
      System.out.println("[Empty]");
      return;
    }

    int tradeCount = 0;
    int profitCount = 0;
    int openCount = 0;
    double totalProfit = 0.0;
    StringBuilder line = new StringBuilder(1024);
    Map stockProfitTable = new TreeMap();
    ProfitStat stockProfit = new ProfitStat();
    ProfitStat monthlyProfit = new ProfitStat();

    ProfitStat allMonthlyProfit = new ProfitStat();
    ProfitStat allYearlyProfit = new ProfitStat();

    for (Trade trade : tradeList)
    {
      line.delete(0,line.length());
      line.append(trade.mDate).append(" | ");
      if (trade.mTransactionType == Trade.TRANS_BUY)
      {
        openCount++;
        line.append("buy   | ");
        line.append(" open: ").append(openCount).append(" | ");
      }
      else if (trade.mTransactionType == Trade.TRANS_SELL) 
      {
        openCount--;
        line.append("sell  | ");
        line.append(" open: ").append(openCount).append(" | ");
      }
      else if (trade.mTransactionType == Trade.TRANS_SHORT) 
      {
        openCount++;
        line.append("short | ");
        line.append(" open: ").append(openCount).append(" | ");
      }
      else if (trade.mTransactionType == Trade.TRANS_COVER) 
      {
        openCount--;
        line.append("cover | ");
        line.append(" open: ").append(openCount).append(" | ");
      }

      String symbol = Mapper.id2Symbol(Integer.toString(trade.mStockId));
      line.append(symbol);
      line.append(" | ");
      line.append(trade.mQuantity).append(" | ");
      line.append(trade.mPrice);
      if (trade.mMatch != null)
      {
        line.append(" | ");
        line.append(trade.mHeld);
        line.append(" | ");
        double profit = trade.getProfit();
        if (profit > 0.0)
        {
          profitCount++;
          profit *= mSimSetting.mProfitAdjustment;
        }
        else
        {
          if (mSimSetting.mMaxLoss < 0.0)
          {
            if (profit < mSimSetting.mMaxLoss)
              profit = mSimSetting.mMaxLoss;
          }
        }

        line.append(NumberUtil.toDecimalFormat(profit));
        line.append(" | ").append(trade.mMatch.mDate);
        line.append(" | rsi2: ").append(trade.mMatch.mRSI);
        totalProfit += profit;
        tradeCount++;

        stockProfit.addProfit(symbol, profit);

        if (!stockProfitTable.containsKey(symbol))
          stockProfitTable.put(symbol, new ProfitStat());
        ProfitStat stockYearlyStat = (ProfitStat) stockProfitTable.get(symbol);
        String yearDate = trade.mDate.substring(0, 4);
        stockYearlyStat.addProfit(yearDate, profit);

        if (trade.mMatch.mDate != null && trade.mMatch.mDate.length() == 10)
        {
          String monthKey = trade.mMatch.mDate.substring(5,7);
          monthlyProfit.addProfit(monthKey, profit);
        }

        String monthDate = trade.mDate.substring(0, trade.mDate.length() - 3);
        allMonthlyProfit.addProfit(monthDate, profit);

        String year = monthDate.substring(0, monthDate.length() - 3);
        allYearlyProfit.addProfit(year, profit);
      }
      System.out.println(line);
    }

    allMonthlyProfit.show();

    showStockProfitTable(stockProfitTable);

    stockProfit.show();
    System.out.println("----");
    stockProfit.show(ProfitStat.SORT_BY_PROFIT);
    monthlyProfit.show();

    allYearlyProfit.show();

    String profitStr = NumberUtil.toCurrencyFormat(totalProfit);
    System.out.println("Total Profit/Loss: " + profitStr);
    double successPercent = (double) profitCount / (double) tradeCount;
    System.out.println("Trades: " + profitCount + " / " + tradeCount + " (" +
                       NumberUtil.toPercentFormat(successPercent) + ")");
  }

  void showStockProfitTable(Map stockProfitTable)
  {
    Iterator iter = stockProfitTable.keySet().iterator();
    while (iter.hasNext())
    {
      String symbol = (String) iter.next();
      ProfitStat profitStat = (ProfitStat) stockProfitTable.get(symbol);
      System.out.println("==========" + symbol + "============");
      profitStat.show();
    }
  }
}
