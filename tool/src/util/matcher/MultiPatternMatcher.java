/*---------------------------------------------------------------------------
| $Id: MultiPatternMatcher.java,v 1.5 2012/06/02 09:33:23 quoc Exp $
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util.matcher;

public class MultiPatternMatcher
{
  PatternMatcher[] m_patternArray=null;

  public MultiPatternMatcher()
  {
  }

  public MultiPatternMatcher(int nSize)
  {
    init(nSize);
  }

  public void init(int nSize)
  {
    m_patternArray=new PatternMatcher[nSize];
  }

  public void set(int i, String stPattern)
  {
    if (m_patternArray!=null)
      m_patternArray[i]=new PatternMatcher(stPattern);
  }

  public int length(int i)
  { 
    if (m_patternArray!=null)
      return m_patternArray[i].length();

    return 0;
  }

  public int read(char cLetter)
  {
    if (m_patternArray==null)
      return 0;

    int nStatus=0;

    for(int i=0; i<m_patternArray.length; i++)
    {
      if (m_patternArray[i].read(cLetter))
        nStatus=i+1;
    }

    return nStatus;
  }

  public void reset()
  {
    if (m_patternArray==null)
      return;

    for(int i=0; i<m_patternArray.length; i++)
      m_patternArray[i].reset();
  }
}
