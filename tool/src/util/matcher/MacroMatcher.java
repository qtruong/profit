/*---------------------------------------------------------------------------
| $Id: MacroMatcher.java,v 1.2 2012/06/02 09:33:23 quoc Exp $
| Copyright (c) 2005 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util.matcher;

public class MacroMatcher
{
  public static AbstractCharMatcher allocate(String macro)
  {
    if (macro.compareTo("digit")==0)
      return new DigitMatcher();
    else if (macro.compareTo("letter")==0)
      return new LetterMatcher();
    else if (macro.compareTo("whitespace")==0)
      return new WhitespaceMatcher();
    else if (macro.compareTo("lowercase")==0)
      return new LowerCaseMatcher();
    else if (macro.compareTo("uppercase")==0)
      return new UpperCaseMatcher();
    else if (macro.compareTo("?")==0)
      return new AnyCharMatcher();
    else
      return null;
  }
}
