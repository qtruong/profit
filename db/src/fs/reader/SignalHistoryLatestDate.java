package com.qtt.db.fs.reader;

import java.io.File;
import com.qtt.db.DBReader;
import com.qtt.db.fs.DBFS;

public class SignalHistoryLatestDate implements DBReader
{
  public Object read(Object data)
  {
    File signalHistoryDir = new File(DBFS.getSignalHistoryDir());
    if (!signalHistoryDir.exists() && !signalHistoryDir.isDirectory())
      return null;

    File[] yearList = signalHistoryDir.listFiles();
    if (yearList == null)
      return null;

    String latestYear = null;
    for (int i=0; i<yearList.length; i++)
    {
      String year = yearList[i].getName();
      if (latestYear == null || latestYear.compareTo(year) < 0)
        latestYear = year;
    }

    if (latestYear == null)
      return null;

    String yearPath = DBFS.getSignalHistoryDir() + latestYear + File.separator;
    File yearDir = new File(yearPath);
    if (!yearDir.exists() || !yearDir.isDirectory())
      return null;

    File[] monthList = yearDir.listFiles();
    if (monthList == null)
      return null;

    String latestMonth = null;
    for (int i=0; i<monthList.length; i++)
    {
      String month = monthList[i].getName();
      if (latestMonth == null || latestMonth.compareTo(month) < 0)
        latestMonth = month;
    }

    if (latestMonth == null)
      return null;

    String monthPath = yearPath + latestMonth + File.separator;
    File monthDir = new File(monthPath);
    if (!monthDir.exists() || !monthDir.isDirectory())
     return null; 

    File[] fileList = monthDir.listFiles();
    if (fileList == null)
      return null;

    String latestDay = null;
    for (int i=0; i<fileList.length; i++)
    {
      String day = fileList[i].getName();
      if (latestDay == null || latestDay.compareTo(day) < 0)
        latestDay = day;
    }

    if (latestDay == null)
      return null;

    int dotIndex = latestDay.indexOf(".");
    latestDay = latestDay.substring(0, dotIndex);
    return latestYear + "-" + latestMonth + "-" + latestDay;
  }
}
