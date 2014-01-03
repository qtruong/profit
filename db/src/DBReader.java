/*---------------------------------------------------------------------------
| $Id: DBReader.java,v 1.4 2012/10/17 00:11:21 quoc Exp $
| Copyright (c) 2012 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db;

public interface DBReader
{
  public Object read(Object criteria);
}
