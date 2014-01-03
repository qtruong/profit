package com.qtt.app.stock.simulator;

import java.util.Map;
import static com.qtt.app.stock.simulator.TriggerCondition.*;

public class ExpressionVariable extends ExpressionBase
{
  String mVariableName;

  public ExpressionVariable(String variableName)
  { 
    mVariableName = variableName;
  }

  public Object getValue(Object data)
  {
    Map signalData = (Map) data; 
    if (getDataType() == DATA_TYPE_INTEGER)
      return (Integer) signalData.get(mVariableName);
    else if (getDataType() == DATA_TYPE_DOUBLE)
      return (Double) signalData.get(mVariableName);

    return null;
  }

  public String toString(){ return "ExpVar: " + mVariableName; } 
}
