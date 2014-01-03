/*---------------------------------------------------------------------------
| $Id: SQLite.java,v 1.1 2013/01/27 22:15:18 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db.sqlite;

import com.qtt.tool.util.ReflectUtil;
import com.qtt.db.DB;
import com.qtt.db.DBReader;
import com.qtt.db.DBWriter;

public class SQLite implements DB
{
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
    String fullName = "com.qtt.db.sqlite." + type + "." + name;
    return ReflectUtil.createObject(fullName);
  }
}
