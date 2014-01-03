package com.qtt.app.stock.simulator;

import java.util.Map;
import static com.qtt.app.stock.simulator.TriggerCondition.*;

public class ExpressionFunction extends ExpressionBase
{
  String mFuncName;
  Expression[] mFuncParam;

  public ExpressionFunction(String funcName, String funcParam)
  {
    mFuncName = funcName;
    String[] paramPart = funcParam.split(",\\s*");
    if (paramPart != null)
    {
      mFuncParam = new Expression[paramPart.length];
      for (int i=0; i<paramPart.length; i++)
      {
        mFuncParam[i] = ExpressionFactory.create(paramPart[i]); 
      }
    }
    setDataType(DATA_TYPE_DOUBLE);
  }

  public Object getValue(Object data)
  {
    Map signalData = (Map) data;

    if (mFuncName.equals("percent_diff"))
    {
      double ma10 = ExpressionUtil.getDouble(mFuncParam[0], signalData);
      double ma5 = ExpressionUtil.getDouble(mFuncParam[1], signalData);
      double percent = (ma10 - ma5) / ma10 * 100.0; 
      Util.mLogger.debug("ma10: {} | ma5: {} | percent: {}",ma10,ma5,percent);

      return new Double(percent);
    }
    else
    {
      Util.mLogger.error("Unknown function: [{}]", mFuncName);
    }

    return null;
  }
}
