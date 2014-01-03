/*---------------------------------------------------------------------------
| $Id: PatternMatcher.java,v 1.6 2012/06/02 09:33:23 quoc Exp $ 
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util.matcher;

//===========================================================================
/**This class can be use to do pattern matching when reading a stream of
 * data byte by byte.  Pattern can be from simple "abc" to complex 
 * "abc[123]". Where:
 *    <pre>
 *    "abc" == "abc"
 *    "abc[123]" == "abc1" 
 *    "abc[123]" == "abc2"
 *    "abc[123]" == "abc3"
 *    </pre>
 * Beside having the bracket to act as "or" expression, there are 
 * various macros.  With the exception of '?' macro, all macros are 
 * expressed in the form "(macro)". There are six macros: digit, letter, 
 * whitespace, uppercase, lowercase, ?.  Ex:
 *    <pre>
 *    "abc(digit)" == "abc0", "abc1", ...., "abc9"
 *    "abc(letter)" == "abca", "abcb", ..., "abcZ"
 *    </pre>
 * Macros can also be inside the bracket expression:
 *    <pre>
 *    "abc[12(letter)] == "abc1", "abc2", "abca", "abcb", ..., "abcZ"
 *    </pre> */
//===========================================================================
public class PatternMatcher
{
  final private static int stateStart=0;
  final private static int stateBackSlash=1;
  final private static int stateBracket=2;
  final private static int stateMacro=3;

  AbstractCharMatcher[] pattern=null;
  int matchIndex;

  //-------------------------------------------------------------------------
  /**Creates an empty pattern. */
  //-------------------------------------------------------------------------
  public PatternMatcher(){}

  //-------------------------------------------------------------------------
  /**Create PatternMatcher object with a pattern. */
  //-------------------------------------------------------------------------
  public PatternMatcher(String p)
  {
    setPattern(p);
  }

  //-------------------------------------------------------------------------
  /**Reset the internal matching index.  Start over. */
  //-------------------------------------------------------------------------
  public void reset()
  {
    matchIndex=0;
  }

  //-------------------------------------------------------------------------
  /**Check if a character will match one of the pattern.
   *
   * @param nIndex  The index of the pattern to match against a character.
   * @param nLetter The character we want to match with.
   * @return  If there is a match, return true; else return false. */
  //-------------------------------------------------------------------------
  public boolean charMatch(int nIndex, char cLetter)
  {
    if (nIndex >= length())
      return false;

    return pattern[nIndex].match(cLetter);
  }

  //-------------------------------------------------------------------------
  /**Set the pattern expression.
   *
   * @param p The pattern expression.
   * @return  If success, return true; else return false */
  //-------------------------------------------------------------------------
  public boolean setPattern(String p)
  {
    if (p==null) return false;

    pattern=new AbstractCharMatcher[p.length()];
    int state=stateStart;
    int previousState=state;
    int patternIndex=0;
    StringBuffer buffer=null;
    StringBuffer macroBuffer=null;

    for(int i=0; i<p.length(); i++)
    {
      switch(state)
      {
      case stateStart:
        if (p.charAt(i)=='\\')
        {
          previousState=state;
          state=stateBackSlash;
        }
        else if (p.charAt(i)=='[')
        {
          buffer=new StringBuffer(16);
          state=stateBracket;
        }
        else if (p.charAt(i)=='(')
        {
          macroBuffer=new StringBuffer(16);
          state=stateMacro;
        }
        else if (p.charAt(i)=='?')
          pattern[patternIndex++]=new AnyCharMatcher();
        else
          pattern[patternIndex++]=new SingleCharMatcher(p.charAt(i));
        break;

      case stateBackSlash:
        if (previousState==stateBracket)
          buffer.append(p.charAt(i));
        else
          pattern[patternIndex++]=new SingleCharMatcher(p.charAt(i));

        state=previousState;
        break;

      case stateBracket:
        if (p.charAt(i)=='\\')
        {
          previousState=state;
          state=stateBackSlash;
        }
        else if (p.charAt(i)==']')
        {
          pattern[patternIndex++]=new MultiCharMatcher(buffer.toString());
          state=stateStart;
        }
        else
          buffer.append(p.charAt(i));
        break;

      case stateMacro:
        if (p.charAt(i)==')')
        {
          pattern[patternIndex++]=MacroMatcher.allocate(macroBuffer.toString());
          state=stateStart;
        }
        else
          macroBuffer.append(p.charAt(i));
        break;
      }
    }

    // Shrink the array if we have over allocated it.
    if (patternIndex < p.length())
    {
      AbstractCharMatcher[] temp=new AbstractCharMatcher[patternIndex];

      for(int i=0; i<patternIndex; i++)
        temp[i]=pattern[i];
      pattern=temp;
    }

    matchIndex=0;
    return true;
  }

  //-------------------------------------------------------------------------
  /**Set the pattern using just a simple string.  No need to add backlash to
   * those special characters. Ex: "\\[".
   *
   * @param stPattern The string.
   * @return  On success, will return true; else return false. */
  //-------------------------------------------------------------------------
  public boolean setStringPattern(String stPattern)
  {
    int nLength=stPattern.length();

    pattern=new AbstractCharMatcher[nLength];

    for(int i=0; i<nLength; i++)
      pattern[i]=new SingleCharMatcher(stPattern.charAt(i));

    return true;
  }

  //-------------------------------------------------------------------------
  /**Read in a single character and determine that the pattern we are
   * looking for has been reached.
   *
   * @param letter  The character to read.
   * @return  If pattern has been reached, return true; else return false. */
  //-------------------------------------------------------------------------
  public boolean read(char letter)
  {
    if (matchIndex >= pattern.length)
      return true;

    if (pattern[matchIndex].match(letter))
    {
      matchIndex++;
      if (matchIndex >= pattern.length)
      {
        matchIndex=0;
        return true;
      }
    }
    else
    {
      for(int i=0; i<matchIndex; i++)
      {
        if (!pattern[i].match(letter))
        {
          // If pattern only partially match, we want to start over.
          // But we would also want to check if current character match
          // the first part of the pattern.
          if (matchIndex!=0 && pattern[0].match(letter))
          {
            matchIndex=1;
            if (matchIndex >= pattern.length)
            {
              matchIndex=0;
              return true;
            }
          }
          else
            matchIndex=0;
          break;
        }
      }

    }

    return false;
  }

  //-------------------------------------------------------------------------
  /**Get the length of the pattern.
   *
   * @return The length of the pattern. */
  //-------------------------------------------------------------------------
  public int length()
  {
    return pattern.length;
  }
}

