/*---------------------------------------------------------------------------
| $Id: SignalHistoryWriter.java,v 1.4 2013/12/30 23:24:21 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db.sqlite.writer;

import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.qtt.db.DBWriter;
import com.qtt.db.common.SQLUtil;
import com.qtt.db.sqlite.SignalHistoryDB;
import com.qtt.tool.util.StringUtil;
import static com.qtt.db.common.SignalFieldConstant.*;

public class SignalHistoryWriter implements DBWriter
{
  final static String TABLE_NAME = "signal_history";

  final static String INSERT_SQL = SQLUtil.genInsertSQL(FIELD_TABLE,TABLE_NAME);
  Object mCurrentId = null;
  static Logger mLogger = LogManager.getLogger("SignalHitsoryWriter");

  public Object write(Object data)
  {
    if (data instanceof Map)
    {
      Map dbData = (Map) data;
      if (mCurrentId == null)
      {
        mCurrentId = dbData.get("id");
        SignalHistoryDB.instance().begin();
      }
      else
      {
        Object id = dbData.get("id");
        if (!id.equals(mCurrentId))
        {
          mLogger.info("currentid: {} | id: {}", mCurrentId, id);
          SignalHistoryDB.instance().commit();
          SignalHistoryDB.instance().begin();
          mCurrentId = id;
        }
      }

      SQLiteStatement st = SignalHistoryDB.instance().getPrepareStatement(INSERT_SQL);
      try
      {
        int index = 1;
        Iterator iter = FIELD_TABLE.keySet().iterator();
        while (iter.hasNext())
        {
          String field = (String) iter.next();
          Object value = dbData.get(field);
          if (value == null)
            st.bindNull(index);
          else if (value instanceof String)
            st.bind(index, (String) value);
          else if (value instanceof Long)
            st.bind(index, ((Long) value).longValue());
          else if (value instanceof Double)
            st.bind(index, ((Double) value).doubleValue());
          else if (value instanceof Integer)
            st.bind(index, ((Integer) value).intValue());
          else
            st.bindNull(index);
          index++;
        }
        st.stepThrough();
        st.reset();
      }
      catch(SQLiteException e)
      {
        throw new RuntimeException(e);
      }
    }
    return null;
  }
}
