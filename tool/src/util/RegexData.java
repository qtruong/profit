/*---------------------------------------------------------------------------
| $Id: RegexData.java,v 1.2 2012/06/02 09:33:22 quoc Exp $
| Copyright (c) 2006 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.util.Vector;

public class RegexData
{
  Vector m_matchData = new Vector(8);
  Vector m_matchStartIndex = new Vector(8);
  Vector m_matchEndIndex = new Vector(8);

  public void add(Object data, int startIndex, int endIndex)
  {
    m_matchData.add(data);
    m_matchStartIndex.add(new Integer(startIndex));
    m_matchEndIndex.add(new Integer(endIndex));
  }

  public String getMatchString(int i)
  {
    return (String) m_matchData.get(i);
  }

  public int getMatchStartIndex(int i)
  {
    return ((Integer) m_matchStartIndex.get(i)).intValue();
  }

  public int getMatchEndIndex(int i)
  {
    return ((Integer) m_matchEndIndex.get(i)).intValue();
  }

  public int size()
  {
    return m_matchData.size();
  }

  public Vector getStringData(){ return m_matchData; }
  public Vector getStartIndexData(){ return m_matchStartIndex; }
  public Vector getEndIndexData(){ return m_matchEndIndex; }
}
