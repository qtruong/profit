package com.qtt.app.stock.simulator;

import com.qtt.tool.util.RegexUtil;
import static com.qtt.app.stock.simulator.TriggerCondition.*;
import static com.qtt.db.common.SignalFieldConstant.FIELD_TABLE;

public class ExpressionFactory
{
  static RegexUtil mFunctionPattern = new RegexUtil("^([a-zA-Z][a-zA-Z0-9_]*)\\(([^\\)]+)\\)");
  static RegexUtil mVariablePatternWithLookBack = new RegexUtil("^([a-zA-z]\\w+)\\s*\\[(\\-?\\d+)\\]");
  static int mDataType = DATA_TYPE_NONE;

  public static Expression create(String expStr)
  {
    String[] functionPart = parseFunction(expStr);
    if (functionPart != null)
    {
      return createFunction(functionPart[0], functionPart[1]);
    }
    else if (isVariable(expStr))
    {
      String[] lookBack = mVariablePatternWithLookBack.getMatch(expStr);
      if (lookBack != null && lookBack.length == 2)
        return createVariableLookBack(lookBack[0], lookBack[1]);
      else
        return createVariable(expStr);
    }
    else
    {
      return createConstant(expStr, mDataType);
    }
  }

  public static Expression createConstant(String expStr, int dataType)
  {
    if (dataType == DATA_TYPE_INTEGER)
    {
      Expression exp = new ExpressionConstant(new Integer(expStr));
      exp.setDataType(dataType);
      return exp;
    }
    else if (dataType == DATA_TYPE_DOUBLE)
    {
      Expression exp = new ExpressionConstant(new Double(expStr));
      exp.setDataType(dataType);
      return exp;
    }

    return null;
  }

  public static Expression createVariableLookBack(String variableName, 
                                              String indexStr)
  {
    Expression exp = new ExpressionVariableLookback(variableName, indexStr);
    exp.setDataType(DATA_TYPE_DOUBLE);
    mDataType = exp.getDataType();
    return exp;
  }

  public static Expression createVariable(String expStr)
  {
    String signalDataType = (String) FIELD_TABLE.get(expStr);
    if (signalDataType == com.qtt.db.common.SignalFieldConstant.DATA_TYPE_DOUBLE)
    {
      Expression exp = new ExpressionVariable(expStr);
      exp.setDataType(DATA_TYPE_DOUBLE);
      mDataType = exp.getDataType();
      return exp;
    }
    else if (signalDataType == com.qtt.db.common.SignalFieldConstant.DATA_TYPE_INTEGER)
    {
      Expression exp = new ExpressionVariable(expStr);
      exp.setDataType(DATA_TYPE_INTEGER);
      mDataType = exp.getDataType();
      return exp;
    }

    return null;
  }

  public static Expression createFunction(String funcName, String funcParam)
  {
    Expression exp = new ExpressionFunction(funcName, funcParam);
    return exp;
  }

  public static String[] parseFunction(String expStr)
  {
    return mFunctionPattern.getMatch(expStr);
  }

  // value is never null and never empty.  a check is already
  // done when reading from config file.
  public static boolean isVariable(String value)
  {
    return Character.isLetter(value.charAt(0));
  }
}
