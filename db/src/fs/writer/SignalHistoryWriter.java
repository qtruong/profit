package com.qtt.db.fs.writer;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.Ostermiller.util.CSVPrinter;
import com.qtt.tool.io.StreamUtil;
import com.qtt.tool.util.FileUtil;
import com.qtt.db.DBWriter;
import com.qtt.db.fs.DBFS;
import static com.qtt.db.common.SignalFieldConstant.*;

public class SignalHistoryWriter implements DBWriter 
{
  public Object write(Object data)
  {
    if (data instanceof Map)
      writeMap((Map) data);
    else if (data instanceof ArrayList)
      writeArrayList((ArrayList) data);

    return null;
  }

  public void writeMap(Map criteria)  
  {
    String filename = getFilename(criteria);

    PrintWriter writer = StreamUtil.openPrintWriter(filename, true);
    CSVPrinter csv = new CSVPrinter(writer);
    writeLine(csv, criteria);
    writer.close();
  }

  public void writeArrayList(ArrayList list)
  {
    // Assuming create_date is the same for each item.
    int size = list.size();
    if (size <= 0)
      return;

    Map data = (Map) list.get(0);
    String filename = getFilename(data);

    PrintWriter writer = StreamUtil.openPrintWriter(filename, true);
    CSVPrinter csv = new CSVPrinter(writer);

    for (int i=0; i<size; i++)
    {
      data = (Map) list.get(i);
      writeLine(csv, data);
    }


    writer.close();

  }

  String getFilename(Map data)
  {
    String date = (String) data.get(CREATE_DATE);

    String[] datePart = date.split("-");
    String dir = DBFS.getSignalHistoryDir() + datePart[0] + File.separator +
                 datePart[1] + File.separator;
    FileUtil.ensureDirExist(dir);
    return dir + datePart[2] + ".csv";
  } 

  void writeLine(CSVPrinter csv, Map data)
  {
    Set keySet = FIELD_TABLE.keySet();
    String[] field = new String[keySet.size()];
    Iterator iter = keySet.iterator();
    int i = 0;
    while (iter.hasNext())
    {
      Object value =  data.get(iter.next());
      if (value != null)
        field[i] = value.toString();
      i++;
    }
    csv.println(field);
  }
}
