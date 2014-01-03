package com.qtt.app.stock.simulator;

public class ExpressionConstant extends ExpressionBase
{
  Object mValue;

  public ExpressionConstant(Object value)
  {
    mValue = value;
  }

  public Object getValue(Object data)
  {
    return mValue;
  }

  public String toString(){ return mValue.toString(); }
}
