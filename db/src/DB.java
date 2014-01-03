/*---------------------------------------------------------------------------
| $Id: DB.java,v 1.3 2012/10/17 00:11:21 quoc Exp $
| Copyright (c) 2012 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db;

public interface DB
{
  public DBReader getReader(String name);
  public DBWriter getWriter(String name);
}
