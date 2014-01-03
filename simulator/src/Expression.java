package com.qtt.app.stock.simulator;

public interface Expression
{
  public Object getValue(Object data);
  public int getDataType();
  public void setDataType(int dataType);
}
