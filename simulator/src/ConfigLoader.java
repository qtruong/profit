package com.qtt.app.stock.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.qtt.tool.util.XMLUtil;

public class ConfigLoader
{
  public final static String START_BALANCE = "start_balance";
  public final static String START_DATE = "start_date";
  public final static String TRADE_AMOUNT = "trade_amount";
  public final static String MAX_STOCK = "max_stock";
  public final static String MAX_TRADE_PER_DAY = "max_trade_per_day";
  public final static String MAX_LOSS = "max_loss";
  public final static String ALLOW_DUPLICATE = "allow_duplicate";
  public final static String HELD = "held";
  public final static String PROFIT_ADJ = "profit_adjustment";

  public void loadConfigFile(String confFile, SimSetting simSetting)
  {
    Document doc = XMLUtil.parseXML(confFile);
    Node root = XMLUtil.getChild(doc, "sim");
    simSetting.clear();
    for (Node child = root.getFirstChild(); child != null;
         child = child.getNextSibling())
    {
      if (!child.getNodeName().equals("setting"))
        continue;

      String name = XMLUtil.getAttribute(child, "name");
      if (name == null)
        continue;

      SimSettingStrategy  strat = new SimSettingStrategy();
      simSetting.mStrategyList.add(strat);
      strat.mName = name;

      simSetting.mStartDate = XMLUtil.getAttribute(child, START_DATE);
      simSetting.mStartBalance = getDoubleAttribute(child, START_BALANCE,
                                                    100000.0);
      simSetting.mTradeAmount = getDoubleAttribute(child, TRADE_AMOUNT,
                                                   20000.0);
      simSetting.mMaxLoss = getDoubleAttribute(child, MAX_LOSS, -1000.0);
      simSetting.mMaxStock = getIntAttribute(child, MAX_STOCK, 25);
      simSetting.mMaxTradePerDay = getIntAttribute(child, MAX_TRADE_PER_DAY,
                                                   10);
      simSetting.mIsAllowDuplicate = getBoolAttribute(child, ALLOW_DUPLICATE,
                                     true);
      simSetting.mHeld = getIntAttribute(child, HELD, -1);
      simSetting.mProfitAdjustment = getDoubleAttribute(child, PROFIT_ADJ, 1.0);

      String symbolData = XMLUtil.getAttribute(child, "symbol");
      addToSymbolList(symbolData, strat);

      parseSetting(strat, child);
    }

  }

  boolean getBoolAttribute(Node child, String attribName, boolean defaultValue)
  {
    String number = XMLUtil.getAttribute(child, attribName);
    if (number == null)
      return defaultValue;

    return Boolean.parseBoolean(number);
  }

  int getIntAttribute(Node child, String attribName, int defaultValue)
  {
    String number = XMLUtil.getAttribute(child, attribName);
    if (number == null)
      return defaultValue;

    return Integer.parseInt(number);
  }

  double getDoubleAttribute(Node child, String attribName, double defaultValue)
  {
    String number = XMLUtil.getAttribute(child, attribName);
    if (number == null)
      return defaultValue;

    return Double.parseDouble(number);
  }

  void parseSetting(SimSettingStrategy stratSetting, Node settingNode)
  {
    for (Node child = settingNode.getFirstChild(); child != null;
         child = child.getNextSibling())
    {
      String nodeName = child.getNodeName();
      if (nodeName.equals("entry"))
      {
        parseEntryExit("entry", stratSetting, child);
      }
      else if (nodeName.equals("exit"))
      {
        parseEntryExit("exit", stratSetting, child);
      }
    }
  }

  void parseEntryExit(String key, SimSettingStrategy stratSetting, Node parent)
  {
    ArrayList triggerDataList = null;
    if (key.equals("entry"))
      triggerDataList = stratSetting.mEntryTriggerList;
    else if (key.equals("exit"))
      triggerDataList = stratSetting.mExitTriggerList;

    for (Node child = parent.getFirstChild(); child != null;
         child = child.getNextSibling())
    {
      String nodeName = child.getNodeName();
      if (nodeName.equals("trigger"))
      {
        String action = XMLUtil.getAttribute(child, "action");
        if (action == null)
          continue;

        TriggerData triggerData = new TriggerData();
        triggerData.mAction = action;
        triggerDataList.add(triggerData);
        parseCondition(triggerData, child);
      }
      else if (nodeName.equals("priority"))
      {
        String type = XMLUtil.getAttribute(child, "type");
        if (type == null)
          continue;

        TradePriority priorityData = null;
        if (type.equals("buy"))
          priorityData = stratSetting.mBuyPriority;
        else if (type.equals("short"))
          priorityData = stratSetting.mShortPriority;

        if (type.equals("buy"))
          priorityData.mType = TradePriority.TYPE_BUY;
        else if (type.equals("short"))
          priorityData.mType = TradePriority.TYPE_SHORT;

        priorityData.mField = XMLUtil.getAttribute(child, "field");
        String orderStr = XMLUtil.getAttribute(child, "order");
        if (orderStr != null)
        {
          if (orderStr.equals("asc"))
            priorityData.mOrder = TradePriority.ORDER_ASC;
          else if (orderStr.equals("desc"))
            priorityData.mOrder = TradePriority.ORDER_DESC;
          else if (orderStr.equals("middle"))
            priorityData.mOrder = TradePriority.ORDER_MIDDLE;
        }

        String symbolStr = XMLUtil.getAttribute(child, "symbol");
        if (symbolStr != null)
        {
          String[] symbolArray = symbolStr.split(",");
          if (symbolArray != null)
          {
            priorityData.mPrioritySymbolList= new ArrayList(symbolArray.length);
            for (int i=0; i<symbolArray.length; i++)
              priorityData.mPrioritySymbolList.add(symbolArray[i]);
          }
        }
      }
    }
  }

  void parseCondition(TriggerData triggerData, Node parent)
  {
    for (Node child = parent.getFirstChild(); child != null;
         child = child.getNextSibling())
    {
      if (!child.getNodeName().equals("condition"))
        continue;

      String value1 = (String) XMLUtil.getAttribute(child, "value1");
      String op = (String) XMLUtil.getAttribute(child, "op");
      String value2 = (String) XMLUtil.getAttribute(child, "value2");
      if (value1 == null || op == null || value2 == null)
      {
        Util.mLogger.warn("value1=[{}] op=[{}] value2=[{}]",value1,op,value2);
        continue;
      }

      value1 = value1.trim();
      op = op.trim();
      value2 = value2.trim();
      if (value1.length() == 0 || op.length() == 0 || value2.length() == 0)
      {
        Util.mLogger.warn("value1=[{}] op=[{}] value2=[{}]",value1,op,value2);
        continue;
      }

      TriggerCondition condition = new TriggerCondition(value1, op, value2);
      triggerData.mConditionList.add(condition);
    }
  }

  void addToSymbolList(String list, SimSettingStrategy simSetting)
  {
    String[] symbolData = list.split("\\s*,\\s*");
    if (symbolData == null)
      return;

    for (int i=0; i<symbolData.length; i++)
      simSetting.mSymbolList.add(symbolData[i].trim());
  }  
}
