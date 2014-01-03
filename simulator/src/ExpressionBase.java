package com.qtt.app.stock.simulator;

public abstract class ExpressionBase implements Expression
{
  int mDataType;

  public int getDataType(){ return mDataType; }
  public void setDataType(int dataType){ mDataType = dataType; }
}
