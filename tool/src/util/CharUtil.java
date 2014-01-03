/*---------------------------------------------------------------------------
| $Id: CharUtil.java,v 1.6 2012/06/02 09:33:19 quoc Exp $ 
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.lang.*;

//--------------------------------------------------------------------------
/**This class contains a collection of character manipulation methods. */
//--------------------------------------------------------------------------
public class CharUtil
{
  private static String otherText="\n\t©\r";

  //-----------------------------------------------------------------------
  /**Check whether a character is a text character.  Text character is 
   * defined as all characters with ASCII code from 32-126. It also
   * includes '\n', '\r', '\t', & the copyright character '©'.
   *
   * @param letter -- The character to test.
   * @return  true, if character is a text character.
   *          false, if character is not a text character. */
  //-----------------------------------------------------------------------
  public static boolean isText(char letter)
  {
    int i;
	
    if (letter>=32 && letter<=126)
    {
      return true;
    }
    else
    {
      int nLength=otherText.length();
      for(i=0; i< nLength; i++)
      {
        if (letter==otherText.charAt(i))
          return true;
        else if ((int)letter == 169)
          return true;
      }
    }
    return false;
  }
}
