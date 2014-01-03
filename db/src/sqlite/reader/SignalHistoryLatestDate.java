/*---------------------------------------------------------------------------
| $Id: SignalHistoryLatestDate.java,v 1.1 2013/06/01 08:42:42 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db.sqlite.reader;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.qtt.db.DBReader;
import com.qtt.db.sqlite.SignalHistoryDB;

public class SignalHistoryLatestDate implements DBReader
{
  public Object read(Object data)
  {
    String sql = "select max(create_date) from signal_history";
    SQLiteConnection db = SignalHistoryDB.instance().getDB();
    SQLiteStatement st = null;
    try
    {
      st = db.prepare(sql);
      st.step();
      return st.columnString(0);
    }
    catch(SQLiteException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      if (st != null)
        st.dispose();
    }
  }
}
