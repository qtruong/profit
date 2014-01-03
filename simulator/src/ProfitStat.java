package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import com.qtt.tool.util.NumberUtil;

public class ProfitStat
{
  public final static String PROFIT = "profit";
  public final static String PROFIT_COUNT = "profitCount";
  public final static String TRADE_COUNT = "tradeCount";
  public final static int SORT_BY_SYMBOL = 1;
  public final static int SORT_BY_PROFIT = 2;

  TreeMap mProfitTable = new TreeMap();

  public void addProfit(String symbol, double profit)
  {
    Map stockProfit = (Map) mProfitTable.get(symbol);
    if (stockProfit == null)
    {
      stockProfit = new HashMap();
      mProfitTable.put(symbol, stockProfit);
    }

    Double dProfit = (Double) stockProfit.get(PROFIT);
    if (dProfit == null)
      stockProfit.put(PROFIT, profit);
    else
      stockProfit.put(PROFIT, dProfit.doubleValue() + profit);

    if (profit > 0.0)
    {
      Integer profitCount = (Integer) stockProfit.get(PROFIT_COUNT);
      if (profitCount == null)
        stockProfit.put(PROFIT_COUNT, 1);
      else
        stockProfit.put(PROFIT_COUNT, profitCount.intValue() + 1);
    }

    Integer tradeCount = (Integer) stockProfit.get(TRADE_COUNT);
    if (tradeCount == null)
      stockProfit.put(TRADE_COUNT, 1);
    else
      stockProfit.put(TRADE_COUNT, tradeCount.intValue() + 1);
  }

  public void show()
  {
    show(SORT_BY_SYMBOL);
  }

  public void show(int sortField)
  {
    ArrayList list = new ArrayList(256);
    Iterator iter = mProfitTable.keySet().iterator();
    while (iter.hasNext())
    {
      String symbol = (String) iter.next();
      Map stockProfit = (Map) mProfitTable.get(symbol);
      stockProfit.put("symbol", symbol);
      list.add(stockProfit);
    }

    if (sortField == SORT_BY_PROFIT)
      Collections.sort(list, new ComparatorRDouble(PROFIT));

    iter = list.iterator();
    while (iter.hasNext())
    {
      Map stockProfit = (Map) iter.next();
      String symbol = (String) stockProfit.get("symbol");

      Integer tradeCount = (Integer) stockProfit.get(TRADE_COUNT);
      if (tradeCount == null)
        continue;

      Integer profitCount = (Integer) stockProfit.get(PROFIT_COUNT);
      if (profitCount == null)
        continue;

      Double profit = (Double) stockProfit.get(PROFIT);
      double percent = (double) profitCount.intValue() /
                        (double) tradeCount.intValue();
      System.out.println(symbol + " | " + 
                         NumberUtil.toCurrencyFormat(profit) + " | " +
                         profitCount + " / " + tradeCount + " - " +
                         NumberUtil.toPercentFormat(percent));
    }
  }
}
