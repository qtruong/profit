/*---------------------------------------------------------------------------
| $Id: SingleCharMatcher.java,v 1.5 2012/06/02 09:33:23 quoc Exp $ 
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util.matcher;

public class SingleCharMatcher extends AbstractCharMatcher
{
  char alpha;

  public SingleCharMatcher()
  { 
    alpha=0; 
  }

  public SingleCharMatcher(char letter)
  { 
    set(letter); 
  }

  public void set(char letter)
  {
    alpha=letter; 
  }

  public boolean match(char letter)
  { 
    return letter==alpha; 
  }
}
