/*---------------------------------------------------------------------------
| $Id: SQLUtil.java,v 1.2 2013/04/29 23:20:31 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SQLUtil
{
  public static String genInsertSQL(Map fieldTable, String tableName)
  {
    Set keySet = fieldTable.keySet();
    String[] columnList = new String[keySet.size()];
    int i=0;
    Iterator iter = keySet.iterator();
    while (iter.hasNext())
    {
      columnList[i] = (String) iter.next();
      i++;
    }
    return genInsertSQL(columnList, tableName);
  }

  public static String genInsertSQL(String[] columnList, String tableName)
  {
    if (columnList == null || columnList.length <= 0)
      return null;

    StringBuffer buffer = new StringBuffer(64);
    StringBuffer question = new StringBuffer(64);
    boolean isFirstTime = true;
    for(int i=0; i<columnList.length; i++)
    {
      appendData(buffer, question, columnList[i], isFirstTime);
      if (isFirstTime)
        isFirstTime = false;
    }

    StringBuffer sql = new StringBuffer(512);
    sql.append("insert into ").append(tableName).append(" (");
    sql.append(buffer).append(") values (").append(question);
    sql.append(")");

    return sql.toString();
  }

  private static void appendData(StringBuffer variable, 
                             StringBuffer value, 
                             CharSequence columnName, boolean isFirst)
  {
    if (!isFirst)
    {
      variable.append(",");
      value.append(",");
    }
    variable.append(columnName);
    value.append("?");
  }
}
