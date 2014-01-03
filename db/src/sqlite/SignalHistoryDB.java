package com.qtt.db.sqlite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class SignalHistoryDB
{
  static String SIGNAL_HISTORY_KEY = "db.sqlite.signal_history";
  static Logger mLogger = LogManager.getLogger("SignalHistoryDB");
  static String mDBFile;

  SQLiteConnection mDB;
  HashMap mCache = new HashMap();

  static SignalHistoryDB mSignalDB;

  public static void setDBFile(String dbFile) { mDBFile = dbFile; }
  public static String getDBFile()
  {
    if (mDBFile == null)
    {
      String configFile = System.getProperty("db.conf");
      mLogger.info("db.conf: [{}]", configFile);
      if (configFile != null)
      {
        // Set mSignalHistory from configuration file
        try
        {
          Properties prop = new Properties();
          prop.load(new FileInputStream(configFile));

          mDBFile = prop.getProperty(SIGNAL_HISTORY_KEY);
        }
        catch(IOException e)
        {
          throw new RuntimeException(e);
        }
      }
    }

    mLogger.info("mDBFile: [{}]", mDBFile);
    return mDBFile;
  }

  public static SignalHistoryDB instance()
  {
    if (mSignalDB == null)
    {
      mSignalDB = new SignalHistoryDB();
      try
      {
        mSignalDB.getDB().open(true);
      }
      catch(SQLiteException e)
      {
        throw new RuntimeException(e);
      }
    }

    return mSignalDB;
  }

  private SignalHistoryDB(){}

  public SQLiteConnection getDB() 
  { 
    if (mDB == null)
      mDB =  new SQLiteConnection(new File(getDBFile()));
      
    return mDB;
  } 

  public void begin()
  {
    try
    {
      getDB().exec("BEGIN");
    }
    catch(SQLiteException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void commit()
  {
    try
    {
      getDB().exec("COMMIT");
    }
    catch(SQLiteException e)
    {
      throw new RuntimeException(e);
    }
  }

  public SQLiteStatement getPrepareStatement(String sql)
  {
    SQLiteStatement st = (SQLiteStatement) mCache.get(sql);
    if (st == null)
    {
      try
      {
        st = getDB().prepare(sql);
      }
      catch(SQLiteException e)
      {
        throw new RuntimeException(e);
      }

      mCache.put(sql, st);
    }
    return st;
  }

  public void close() 
  { 
    if (mDB != null)
    {
      Iterator iter = mCache.keySet().iterator();
      while (iter.hasNext())
      {
        SQLiteStatement st = (SQLiteStatement) mCache.get(iter.next());
        st.dispose();
      }
      mCache.clear();
      mDB.dispose();
      mDB = null;
    }
  }
}
