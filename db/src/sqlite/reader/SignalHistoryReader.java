/*---------------------------------------------------------------------------
| $Id: SignalHistoryReader.java,v 1.4 2013/12/31 14:58:51 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db.sqlite.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.qtt.db.DBHandler;
import com.qtt.db.DBReader;
import com.qtt.db.sqlite.SignalHistoryDB;
import com.qtt.db.param.SignalHistoryParam;
import static  com.almworks.sqlite4java.SQLiteConstants.*;

public class SignalHistoryReader implements DBReader
{
  public Object read(Object data)
  {
    if (! (data instanceof SignalHistoryParam))
      return null;

    SignalHistoryParam param = (SignalHistoryParam) data;
    if (param.mStartDate == null || param.mDBHandler == null)
      return null;

    String sql = "select * from signal_history where create_date >= ? order by create_date";

    SQLiteConnection db = SignalHistoryDB.instance().getDB();
    SQLiteStatement st = null;

    ArrayList signalList = new ArrayList(5000);
    String lastDate = null;
    try
    {
      st = db.prepare(sql);
      st.bind(1, param.mStartDate);

      while (st.step())
      {
        Map signalData = getRow(st);
        if (signalData == null)
          continue;

        if (lastDate == null)
        {
          lastDate = (String) signalData.get("create_date");
        }
        else 
        {
          String date = (String) signalData.get("create_date");
          if (!date.equals(lastDate))
          {
            param.mDBHandler.handleData(signalList);
            signalList.clear();
            lastDate = date;
          }
        }

        signalList.add(signalData);
      }

      param.mDBHandler.handleData(signalList);
    }
    catch(SQLiteException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      st.dispose();
    }

    return signalList;
  }

  Map getRow(SQLiteStatement st) throws SQLiteException
  {
    HashMap data = new HashMap();
    int columnCount = st.columnCount();
    for (int i=0; i<columnCount; i++)
    {
      String columnName = st.getColumnName(i);
      if (st.columnType(i) == SQLITE_INTEGER)
        data.put(columnName, st.columnLong(i));
      else if (st.columnType(i) == SQLITE_FLOAT)
        data.put(columnName, st.columnDouble(i));
      else if (st.columnType(i) == SQLITE_TEXT)
        data.put(columnName, st.columnString(i));
      else if (st.columnType(i) == SQLITE_NULL)
        data.put(columnName, null);
    }

    return data;
  }
}
