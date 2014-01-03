package com.qtt.db.fs.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.Ostermiller.util.CSVParser;
import com.qtt.tool.io.StreamUtil;
import com.qtt.tool.util.NumberUtil;
import com.qtt.tool.util.RegexUtil;
import com.qtt.db.DBHandler;
import com.qtt.db.DBReader;
import com.qtt.db.param.SignalHistoryParam;
import com.qtt.db.fs.DBFS;
import static com.qtt.db.common.SignalFieldConstant.*;

public class SignalHistoryReader implements DBReader
{
  final static String[] MONTH = 
  {
    "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
  };

  String mSimPath;

  String[] mDatePart;  
  ArrayList<File> mTimeFileList = new ArrayList<File>(10240);

  ArrayList mSignalDataList = new ArrayList(5000);

  public Object read(Object data)
  {
    if (! (data instanceof SignalHistoryParam))
      return null;

    SignalHistoryParam param = (SignalHistoryParam) data;
    if (param.mStartDate == null || param.mDBHandler == null)
      return null;

    generateTimeFileList(DBFS.getSignalHistoryDir(), param.mStartDate);

    for (File timeFile : mTimeFileList)
    {
      mSignalDataList.clear();
      loadSignal(timeFile, mSignalDataList);
      param.mDBHandler.handleData(mSignalDataList);
    }

    return null;
  }

  String getDate(File timeFile)
  {
    String filename = timeFile.getAbsolutePath();
    String[] datePart = RegexUtil.extractMatch(filename, "(\\d+)\\\\(\\d+)\\\\(\\d+)\\.csv$");
    if (datePart != null && datePart.length == 3)
      return datePart[0] + "-" + datePart[1] + "-" + datePart[2]; 

    return null;
  }

  void loadSignal(File timeFile, ArrayList signalDataList)
  {
    String date = getDate(timeFile);
    BufferedReader reader = StreamUtil.openBufferedReader(timeFile);
    String[][] values = null;
    try
    {
      values = CSVParser.parse(reader);
      if (values != null)
      {
        for (int i=0; i<values.length; i++)
        {
          Map signalData = new HashMap();
          Iterator iter = FIELD_TABLE.keySet().iterator();
          int index = 0;
          while (iter.hasNext())
          {
            Object key = iter.next();

            Object dataType = FIELD_TABLE.get(key);
            if (dataType == DATA_TYPE_STRING)
              signalData.put(key, values[i][index].toString());
            else if (dataType == DATA_TYPE_INTEGER)
              signalData.put(key, NumberUtil.toInt(values[i][index]));
            else if (dataType == DATA_TYPE_LONG)
              signalData.put(key, NumberUtil.toLong(values[i][index]));
            else if (dataType == DATA_TYPE_FLOAT)
              signalData.put(key, NumberUtil.toFloat(values[i][index]));
            else if (dataType == DATA_TYPE_DOUBLE)
              signalData.put(key, NumberUtil.toDouble(values[i][index]));

            index++;
          }
          signalData.put(CREATE_DATE, date);
          signalDataList.add(signalData);
        }
      }
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      StreamUtil.close(reader);
    }
  }

  public ArrayList generateTimeFileList(String simPath, String startDate)
  {
    mSimPath = simPath;
    mTimeFileList.clear();
    File[] dirList = new File(mSimPath).listFiles();
    if (dirList == null)
      return null;

    ArrayList yearList = new ArrayList(64);
    mDatePart = startDate.split("\\-");
    for (int i=0; i<dirList.length; i++)
    {
      String dirName = dirList[i].getName();
      if (dirName.compareTo(mDatePart[0]) >= 0)
        yearList.add(dirName);
    }
    Collections.sort(yearList);

    int yearSize = yearList.size();
    String year = (String) yearList.get(0);
    iterateMonth(year, true);
    for (int i=1; i<yearSize; i++)
    {
      year = (String) yearList.get(i);
      iterateMonth(year, false);
    }

    return mTimeFileList;
  }

  void iterateMonth(String year, boolean isCheckMonth)
  {
    for (int i=0; i<MONTH.length; i++)
    {
      if (isCheckMonth)
      {
        if (MONTH[i].compareTo(mDatePart[1]) >= 0)
        {
          iterateDay(year, MONTH[i], true);
        }
      }
      else
      {
        iterateDay(year, MONTH[i], false);
      }
    }
  }

  void iterateDay(String year, String month, boolean isCheckDay)
  {
    String dir = mSimPath + year + File.separator + month + File.separator;
    File[] csvFileList = new File(dir).listFiles();
    if (csvFileList == null)
      return;

    for (int i=0; i<csvFileList.length; i++)
    {
      if (isCheckDay)
      {
        if (csvFileList[i].getName().compareTo(mDatePart[2]) >= 0)
          mTimeFileList.add(csvFileList[i]);
      }
      else
      {
        mTimeFileList.add(csvFileList[i]);
      }
    }
  }
}
