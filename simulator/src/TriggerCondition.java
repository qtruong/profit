package com.qtt.app.stock.simulator;

import java.util.Map;
import com.qtt.tool.util.NumberUtil;
import com.qtt.tool.util.RegexUtil;
import static com.qtt.db.common.SignalFieldConstant.FIELD_TABLE;

public class TriggerCondition
{
  public final static int DATA_TYPE_NONE = 0;
  public final static int DATA_TYPE_DOUBLE = 1;
  public final static int DATA_TYPE_LONG = 2;
  public final static int DATA_TYPE_STRING = 3;
  public final static int DATA_TYPE_INTEGER = 4;

  String mOp;

  Expression mValue1;
  Expression mValue2;
  int mDataType = DATA_TYPE_NONE;

  public TriggerCondition(String exp1, String op, String exp2)
  {
    mValue1 = ExpressionFactory.create(exp1);
    mValue2 = ExpressionFactory.create(exp2);
    mOp = op;
  }

  public String toString()
  {
    return "Value1: " + mValue1 + "\n" +
           "Value2: " + mValue2 + "\n" +
           "Op: " + mOp + "\n";
  }

  public boolean isConditionTrue(Map signal)
  {
    int dataType = getDataType(); 
    if (dataType == DATA_TYPE_DOUBLE)
    {
      double number1 = ExpressionUtil.getDouble(mValue1, signal);
      double number2 = ExpressionUtil.getDouble(mValue2, signal);
      Util.mLogger.info("{} vs. {}", number1, number2);
      
      if (mOp.equals("lt"))
        return number1 < number2;
      else if (mOp.equals("gt"))
        return number1 > number2;
      else if (mOp.equals("eq"))
        return number1 == number2;
      else if (mOp.equals("lte"))
        return number1 <= number2;
      else if (mOp.equals("gte"))
        return number1 >= number2;
    }
    else if (dataType == DATA_TYPE_INTEGER)
    {
      int number1 = ExpressionUtil.getInteger(mValue1, signal);
      int number2 = ExpressionUtil.getInteger(mValue2, signal);
      Util.mLogger.info("{} vs. {}", number1, number2);
      
      if (mOp.equals("lt"))
        return number1 < number2;
      else if (mOp.equals("gt"))
        return number1 > number2;
      else if (mOp.equals("eq"))
        return number1 == number2;
      else if (mOp.equals("lte"))
        return number1 <= number2;
      else if (mOp.equals("gte"))
        return number1 >= number2;
    }

    return false;
  }

  int getDataType()
  {
    int dataType1 = mValue1.getDataType();
    int dataType2 = mValue2.getDataType();
    if (dataType1 == DATA_TYPE_INTEGER && dataType2 == DATA_TYPE_INTEGER)
      return DATA_TYPE_INTEGER;
    else if (dataType1 == DATA_TYPE_DOUBLE || dataType2 == DATA_TYPE_DOUBLE)
      return DATA_TYPE_DOUBLE;
    
    return dataType1;
  }
}
