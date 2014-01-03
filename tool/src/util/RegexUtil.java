/*---------------------------------------------------------------------------
| $Id: RegexUtil.java,v 1.9 2012/06/02 09:33:22 quoc Exp $
| Copyright (c) 2002 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil
{
  Pattern m_pattern;

  public RegexUtil(){}
  public RegexUtil(String pattern){ setPattern(pattern); }

  public void setPattern(String pattern)
  {
    m_pattern = Pattern.compile(pattern);
  }

  public boolean isMatch(String text)
  {
    Matcher matcher = m_pattern.matcher(text);
    return matcher.find();
  }

  public String[] getMatch(String text)
  {
    return extractMatch(text, m_pattern);
  }

  public static boolean isPatternMatch(String pattern, String str)
  {
    RegexUtil regex = new RegexUtil();
    regex.setPattern(pattern);
    return regex.isMatch(str);
  }

  public static String[] extractMatch(String str, String regex)
  {
    Pattern pattern = Pattern.compile(regex);
    return extractMatch(str, pattern);
  }

  public static String[] extractMatch(String str, Pattern pattern)
  {
    Vector data = new Vector(8); 
    Matcher matcher = pattern.matcher(str);
    while (matcher.find())
    {
      int size = matcher.groupCount();
      for(int i=0; i<size; i++)
        data.add(matcher.group(i+1));
    }

    int size = data.size();
    if (size == 0)
      return null;

    String[] part = new String[size];
    for (int i=0; i<size; i++)
      part[i] = (String) data.get(i);
    
    return part;
  }

  public static RegexData extractMatchFull(String str, String regex)
  {
    Pattern pattern = Pattern.compile(regex);
    return extractMatchFull(str, pattern);
  }
  
  public static RegexData extractMatchFull(String str, Pattern pattern)
  {
    RegexData regexData = new RegexData();
    Matcher matcher = pattern.matcher(str);
    while (matcher.find())
    {
      int size = matcher.groupCount();
      for(int i=0; i<size; i++)
        regexData.add(matcher.group(i+1), matcher.start(i+1), matcher.end(i+1));
    }

    int size = regexData.size();
    if (size == 0)
      return null;

    return regexData;
  }
}
