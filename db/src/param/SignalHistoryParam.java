/*---------------------------------------------------------------------------
| $Id: SignalHistoryParam.java,v 1.4 2013/07/18 10:09:42 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db.param;

import com.qtt.db.DBHandler;

public class SignalHistoryParam
{
  public String mStartDate;
  public DBHandler mDBHandler;

  public String toString()
  {
    return "StartDate: " + mStartDate + " | DBHandler: " + mDBHandler;
  }
}
