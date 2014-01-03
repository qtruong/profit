package com.qtt.app.stock.simulator;

import java.util.Map;

public class ExpressionVariableLookback extends ExpressionBase
{
  String mVariableName;
  int mIndex;

  public ExpressionVariableLookback(String variableName, String indexStr)
  { 
    mVariableName = variableName;
    mIndex = Integer.parseInt(indexStr);
  }

  public Object getValue(Object data)
  {
    Map lookBackData = LookBack.instance().get(mIndex);
    if (lookBackData == null)
      return null;

    Map signalData = (Map) data; 
    if (signalData == null)
      return null;

    Object id = signalData.get("id");
    if (id == null)
      return null;

    // Getting the old signal for the stock
    Map oldSignal = (Map) lookBackData.get(id);
    if (oldSignal == null)
    {
      Util.mLogger.info("oldSignal doesn't exists for id: {}", id);
      return null;
    }

    // Getting the old signal itself
    Double d = (Double) oldSignal.get(mVariableName);
    if (d == null)
    {
      Util.mLogger.error("null for [{}]", mVariableName);
      return null;
    }

    return d.doubleValue();
  }

  public String toString()
  {
    return "ExpVar: " + mVariableName + "[" + mIndex + "]";
  } 
}
