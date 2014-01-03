package com.qtt.db.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qtt.tool.util.ReflectUtil;
import com.qtt.db.DB;
import com.qtt.db.DBReader;
import com.qtt.db.DBWriter;

public class DBFS implements DB
{
  final static String SIGNAL_HISTORY_KEY = "db.fs.signal_history_dir";
  static Logger mLogger = LogManager.getLogger("DBFS");
  static String mSignalHistoryDir;

  public DBReader getReader(String name)
  {
    return (DBReader) get(name, "reader");
  }

  public DBWriter getWriter(String name)
  {
    return (DBWriter) get(name, "writer");
  }

  Object get(String name, String type)
  {
    String fullName = "com.qtt.db.fs." + type + "." + name;
    return ReflectUtil.createObject(fullName);
  }

  public static String getSignalHistoryDir()
  {
    if (mSignalHistoryDir == null)
    {
      // mSignalHistoryDir is set from either a configuration file or
      // from system properties itself.
      String configFile = System.getProperty("db.conf");
      mLogger.info("db.conf: [{}]", configFile);
      if (configFile != null)
      {
        // Set mSignalHistory from configuration file
        try
        {
          Properties prop = new Properties();
          prop.load(new FileInputStream(configFile));

          String dir = prop.getProperty(SIGNAL_HISTORY_KEY);
          mLogger.info("{}: [{}]", SIGNAL_HISTORY_KEY, dir);
          mSignalHistoryDir = ensureDirPath(dir);
        }
        catch(IOException e)
        {
          throw new RuntimeException(e);
        }
      }
      else
      {
        // Set mSignalHistory from system properties
        String dir = System.getProperty(SIGNAL_HISTORY_KEY);
        mLogger.info("{}: [{}]", SIGNAL_HISTORY_KEY, dir);
        mSignalHistoryDir = ensureDirPath(dir);
      }
    }

    mLogger.info("mSignalHistoryDir: [{}]", mSignalHistoryDir);

    return mSignalHistoryDir;
  }

  static String ensureDirPath(String dir)
  {
    if (dir != null && !dir.endsWith(File.separator))
      return dir = dir + File.separator;

    return dir;
  }
}
