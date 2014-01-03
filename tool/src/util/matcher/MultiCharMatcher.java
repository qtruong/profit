/*---------------------------------------------------------------------------
| $Id: MultiCharMatcher.java,v 1.2 2012/06/02 09:33:23 quoc Exp $
| Copyright (c) 2005 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util.matcher;

public class MultiCharMatcher extends AbstractCharMatcher
{
  final private int stateStart=0;
  final private int stateMacro=3;

  AbstractCharMatcher[] orArray;

  public MultiCharMatcher()
  { 
    orArray=null; 
  }

  public MultiCharMatcher(String exp)
  { 
    set(exp);
  }

  public void set(String exp)
  {
    if (exp==null) return;

    orArray=new AbstractCharMatcher[exp.length()];
    int arrayIndex=0;
    int state=stateStart;
    StringBuffer macroBuffer=null;
    for(int i=0; i<exp.length(); i++)
    {
      switch(state)
      {
      case stateStart:
        if (exp.charAt(i)=='(')
        {
          macroBuffer=new StringBuffer(16);
          state=stateMacro;
        }
        else
          orArray[arrayIndex++]=new SingleCharMatcher(exp.charAt(i));
        break;

      case stateMacro:
        if (exp.charAt(i)==')')
        {
          orArray[arrayIndex++]=MacroMatcher.allocate(macroBuffer.toString());
          state=stateStart;
        }
        else
          macroBuffer.append(exp.charAt(i));
        break;
      }
    }

    // Shrink to fit.
    if (arrayIndex < exp.length())
    {
      AbstractCharMatcher[] tempArray=new AbstractCharMatcher[arrayIndex];
      for(int i=0; i<arrayIndex; i++)
        tempArray[i]=orArray[i];
      orArray=tempArray;
    }
  }

  public boolean match(char letter)
  {
    for(int i=0; i<orArray.length; i++)
    {
      if (orArray[i].match(letter))
        return true;
    }

    return false;
  }
}
