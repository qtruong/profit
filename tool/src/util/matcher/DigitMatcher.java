/*---------------------------------------------------------------------------
| $Id: DigitMatcher.java,v 1.2 2012/06/02 09:33:23 quoc Exp $
| Copyright (c) 2005 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util.matcher;

public class DigitMatcher extends AbstractCharMatcher
{
  public void set(String pattern){}

  public boolean match(char letter)
  {
    return Character.isDigit(letter);
  }
}
