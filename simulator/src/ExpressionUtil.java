package com.qtt.app.stock.simulator;
import java.util.Map;

public class ExpressionUtil
{
  public static double getDouble(Expression exp, Map signalData)
  {
    Double number = (Double) exp.getValue(signalData);
    if (number == null)
    {
      Util.mLogger.error("Null in double expression: {}", exp);
      return 0.0;
    }

    return number.doubleValue();
  }

  public static int getInteger(Expression exp, Map signalData)
  {
    Integer number = (Integer) exp.getValue(signalData);
    if (number == null)
    {
      Util.mLogger.error("Null in Integer expression: {}", exp);
      return 0;
    }

    return number.intValue();
  }
}
