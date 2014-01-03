/*---------------------------------------------------------------------------
| $Id: StringUtil.java,v 1.31 2012/06/02 09:33:22 quoc Exp $ 
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import com.qtt.tool.util.matcher.PatternMatcher;

//===========================================================================
/** This class contains a collection of string manipulation methods. */
//===========================================================================
public class StringUtil
{
  public final static char DEFAULT_SEPARATOR_CHAR = ',';

  public static boolean contains(String line, String pattern)
  {
    if (line.indexOf(pattern) == -1)
      return false;

    return true;
  }

  public static String[] split(String stLine)
  {
    StringTokenizer tokenizer = new StringTokenizer(stLine);
    String[] part = new String[tokenizer.countTokens()];
    int i=0;
    while(tokenizer.hasMoreTokens())
    {
      part[i++] = tokenizer.nextToken();
    }

    return part;
  }

  public static String join(String[] data)
  {
    return join(DEFAULT_SEPARATOR_CHAR, data);
  }

  public static String join(char separator, String[] data)
  {
    return join(Character.toString(separator), data, 0, -1);
  }

  public static String join(String separator, String[] data)
  {
    return join(separator, data, 0, -1);
  }

  public static String join(String separator, String[] data, int start, int end)
  {
    int size = data.length;
    if (size <= 0)
      return "";

    if (end == -1)
      end = size - 1;

    StringBuffer buffer = new StringBuffer(64);
    buffer.append(data[start]);
    for(int i=start+1; i<=end; i++)
      buffer.append(separator).append(data[i]);

    return buffer.toString();
  }

  public static String join(ArrayList data)
  {
    return join(DEFAULT_SEPARATOR_CHAR, data);
  }

  public static String join(char separator, ArrayList vector)
  {
    return join(Character.toString(separator), vector);
  }

  public static String join(String separator, ArrayList vector)
  {
    int size = vector.size();
    if (size <= 0)
      return "";

    StringBuffer buffer = new StringBuffer(64);
    buffer.append(vector.get(0).toString());
    for(int i=1; i<size; i++)
      buffer.append(separator).append(vector.get(i).toString());

    return buffer.toString();
  }

  public static String join(Vector data)
  {
    return join(DEFAULT_SEPARATOR_CHAR, data);
  }

  public static String join(char separator, Vector vector)
  {
    return join(Character.toString(separator), vector);
  }

  public static String join(String separator, Vector vector)
  {
    int size = vector.size();
    if (size <= 0)
      return "";

    StringBuffer buffer = new StringBuffer(64);
    buffer.append(vector.get(0).toString());
    for(int i=1; i<size; i++)
      buffer.append(separator).append(vector.get(i).toString());

    return buffer.toString();
  }

  public static String join(Map data)
  {
    return join(DEFAULT_SEPARATOR_CHAR, data);
  }

  public static String join(char separator, Map data)
  {
    Iterator iterator = data.keySet().iterator();
    return join(separator, iterator);
  }

  public static String join(Set data)
  {
    return join(DEFAULT_SEPARATOR_CHAR, data);
  }

  public static String join(char separator, Set data)
  {
    Iterator iterator = data.iterator();
    return join(separator, iterator);
  }

  public static String join(List data)
  {
    return join(DEFAULT_SEPARATOR_CHAR, data);
  }

  public static String join(char separator, List data)
  {
    return join(separator, data.iterator());
  }

  public static String join(Iterator iterator)
  {
    return join(DEFAULT_SEPARATOR_CHAR, iterator);
  }

  public static String join(char separator, Iterator iterator)
  {
    if (!iterator.hasNext())
      return "";

    StringBuffer buffer = new StringBuffer(64);
    buffer.append((String)iterator.next());
    while(iterator.hasNext())
      buffer.append(separator).append((String)iterator.next());

    return buffer.toString();
  }

  public static void append(StringBuffer buffer, String text, String separator)
  {
    if (buffer.length() > 0)
      buffer.append(separator);
    buffer.append(text);
  }    

  //-----------------------------------------------------------------------
  /**Get the index of the first digit in the string.
   *
   * @param stLine A string.
   * @return The index of the first digit in the string. */
  //-----------------------------------------------------------------------
  public static int firstDigit(String stLine)
  {
    int nLength=stLine.length();
    for(int i=0; i<nLength; i++)
    {
      if (Character.isDigit(stLine.charAt(i)))
        return i;
    }
 
    return -1;
  }

  //-----------------------------------------------------------------------
  /**Get the index of the first non-digit in the string.
   *
   * @param stLine A string.
   * @return The index of the first non-digit in the string. */
  //-----------------------------------------------------------------------
  public static int firstNonDigit(String stLine)
  {
    int nLength=stLine.length();
    for(int i=0; i<nLength; i++)
    {
      if (!Character.isDigit(stLine.charAt(i)))
        return i;
    }
 
    return -1;
  }

  //-----------------------------------------------------------------------
  /**Get the index of the last digit in the string.
   *
   * @param stLine A string.
   * @return The index of the last digit in the string. */
  //-----------------------------------------------------------------------
  public static int lastDigit(String stLine)
  {
    int nLength=stLine.length();
    for(int i=nLength-1; i>=0; i--)
    {
      if (Character.isDigit(stLine.charAt(i)))
        return i;
    }

    return -1;
  }

  //-----------------------------------------------------------------------
  /**Get the index of the last non-digit in the string.
   *
   * @param stLine A string.
   * @return The index of the last non-digit in the string. */
  //-----------------------------------------------------------------------
  public static int lastNonDigit(String stLine)
  {
    int nLength=stLine.length();
    for(int i=nLength-1; i>=0; i--)
    {
      if (!Character.isDigit(stLine.charAt(i)))
        return i;
    }

    return -1;
  }

  //-----------------------------------------------------------------------
  /**Convert a text string with multiple lines into an array of strings.
   * 
   * @param stText A text string.
   * @return An array of strings with each line in one index. */
  //-----------------------------------------------------------------------
  public static String[] toStringArray(String stText)
  {
    StringTokenizer tokenizer=new StringTokenizer(stText,"\r\n");
    int nSize=tokenizer.countTokens();
    if (nSize<=0)
      return null;

    String[] buffer=new String[nSize];

    for(int i=0; i<nSize; i++)
    {
      buffer[i]=tokenizer.nextToken();
    }

    return buffer;
  }

  //-----------------------------------------------------------------------
  /**Convert a specified character to File.separatorChar. 
   *
   * @param buffer The StringBuffer object containing the string.
   * @param separator  The character to be replaced by File.separatorChar.
   */
  //-----------------------------------------------------------------------
  public static boolean toSysSeparator(StringBuffer buffer,char separator) 
  {
    boolean bIsModified=false;

    int nLength=buffer.length();
    for(int i=0; i<nLength; i++)
    {
      if (buffer.charAt(i)==separator)
      {
        buffer.setCharAt(i,File.separatorChar);
        bIsModified=true;
      }
    }

    return bIsModified;
  }

  //-----------------------------------------------------------------------
  /**Replace all File.separatorChar characters to a specified character. 
   *
   * @param buffer The StringBuffer object containing the string.
   * @param separator  The character to replace the File.separatorChar. */
  //-----------------------------------------------------------------------
  public static boolean repSysSeparator(StringBuffer buffer, char separator)
  {
    boolean bIsModified=false;
    int nLength=buffer.length();
    for(int i=0; i<nLength; i++)
    {
      if (buffer.charAt(i)==File.separatorChar)
      {
        buffer.setCharAt(i,separator);
        bIsModified=true;
      }
    }

    return bIsModified;
  }

  //-----------------------------------------------------------------------
  /**Replace all File.separatorChar characters to a specified character. 
   * This is exactly the same as repSysSeparator() except this one deals
   * with String instead of StringBuffer.
   *
   * @param stLine The a string.
   * @param separator  The character to replace the File.separatorChar. 
   * @return The modified string. */
  //-----------------------------------------------------------------------
  public static String repSysSeparator2(String stLine, char separator)
  {
    StringBuffer buffer=new StringBuffer(64);
    buffer.append(stLine);
    if (repSysSeparator(buffer,separator))
      return buffer.toString();

    return stLine; 
  }

  //-----------------------------------------------------------------------
  /**Check if the string is blank.  A string is blank when is doesn't 
   * contains any alpha-numeric characters.
   *
   * @param stLine A string to check if it's blank.
   * @return If the string is blank, return true; else return false. */
  //-----------------------------------------------------------------------
  public static boolean isBlank(String stLine)
  {
    return stLine.trim().length()==0;
  }

  //-----------------------------------------------------------------------
  /**Find the first alphabet character in the string. 'A'-'z'.
   * 
   * @param source  The string we are searching.
   * @return The index of the first alphabet character found. Returns -1
   *         if an alphabet character doesn't exists. */  
  //-----------------------------------------------------------------------
  public static int findFirstLetter(String stSource)
  {
    int nLength=stSource.length();
    for(int i=0; i<nLength; i++)
    {
      if (Character.isLetter(stSource.charAt(i)))
        return i;
    }
    return -1;
  }

  //-----------------------------------------------------------------------
  /**Get a directory name from a path name.
   *
   * @param source -- String contain path name.
   * @return  On success, return a directory name.
   *      On failure, return null. */
  //-----------------------------------------------------------------------
  public static String dirName(String path)
  {
    String dirPath = dirName(path, File.separatorChar);
    if (dirPath != ".")
      return dirPath;

    char[] separatorArray = {'\\', '/'};
    for(int i=0; i<separatorArray.length; i++)
    {
      dirPath = dirName(path,separatorArray[i]);
      if (dirPath != ".")
        return dirPath;
    }
    return ".";
  }

  public static String dirName(String path, char separator)
  {
    path = path.trim();

    int length = path.length();
    if (length<=0)
      return null;

    int index=path.lastIndexOf(separator);

    if (index>0 && index==length-1)
    {
      index=path.lastIndexOf(separator,length-2);
    }

    if (index == -1)
      return ".";

    if (index==0)   
      return path.substring(0,1);

    return path.substring(0,index);
  }

  //-----------------------------------------------------------------------
  /**Get the file name from a path name.
   *
   * @param source -- The path name.
   * @return  On success, will return the file name.
   *      On failure, will return null. */
  //-----------------------------------------------------------------------
  public static String baseName(String source)
  {
    if (source.length()<=0) return null;

    int i=source.lastIndexOf(File.separator);

    if (i==-1) 
    {
      if ( (i=source.lastIndexOf('\\')) == -1)
      {
        if ( (i=source.lastIndexOf('/')) == -1)
          return source;
      }
    }

    return source.substring(i+1,source.length());
  }

  //-----------------------------------------------------------------------
  /**Convert all tab characters to space.
   *
   * @param strLine -- The string that we are going to convert tab characters
   *                   to space.
   * @param tabStop -- The # of spaces each tab character should expand.
   * @return  Return a string with tabs that was expanded to spaces. */
  //-----------------------------------------------------------------------
  public static long tab2Space(StringBuffer sbStrLine, int nTabStop)
  {
    int nLength=sbStrLine.length();
    int nCurrentTab=0;
    long nTotalTabs=0;
    String stBuffer=sbStrLine.toString();
    
    sbStrLine.delete(0,sbStrLine.length());
    for(int i=0; i<nLength; i++)
    {
      if ( stBuffer.charAt(i) == '\t' )
      {
        for(;nCurrentTab<nTabStop; nCurrentTab++)
          sbStrLine.append(' ');
        nCurrentTab=-1;
        nTotalTabs++;
      }
      else 
        sbStrLine.append(stBuffer.charAt(i));

      nCurrentTab=(nCurrentTab+1)%nTabStop;
    }

    return nTotalTabs;
  }

  //-----------------------------------------------------------------------
  /**Get rid of a character from a string.
   *
   * @param strLine -- The string we are going to get rid of a specified char.
   * @param letter -- The character specified.
   * @return  The string that is the result of this method.  One less 
   *          character if such a character exists in 'strLine'. */
  //-----------------------------------------------------------------------
  public static String ridChar(String strLine, char letter)
  {
    int i=strLine.indexOf(letter);

    if (i==-1) return strLine;
    StringBuffer temp=new StringBuffer(strLine.substring(0,i));
    temp.append(strLine.substring(i+1,strLine.length())); 
    
    return temp.toString(); 
  }

  //-----------------------------------------------------------------------
  /**Rid all occurance a character from a string.
   *
   * @param strLine -- The string we are going to get rid of a specified char.
   * @param letter -- The character specified.
   * @return  The string that is the result of this method.  One less 
   *          character if such a character exists in 'strLine'. */
  //-----------------------------------------------------------------------
  public static String ridAllChar(String strLine, char letter)
  {
    int i=strLine.indexOf(letter);
    if (i==-1) return strLine;

    int nLength = strLine.length();
    StringBuffer temp=new StringBuffer(nLength);
    for(i=0; i<nLength; i++)
    {
      if (strLine.charAt(i) != letter)
      {
        temp.append(strLine.charAt(i));
      }
    }
    
    return temp.toString(); 
  }


  //-----------------------------------------------------------------------
  /**Get the number of fields in a string.
   *
   * @param src -- String containing many fields.
   * @return  On success, will return an int specifying how many fields 
   *            there are.  
   *          On failure, will return 0. */
  //-----------------------------------------------------------------------
  public static int numField(String src)
  {
    int state=0;
    int numberField=0;
    int length=src.length();

    // String must have something 
    if (length<=0)
      return 0;

    for(int j=0; j<length; j++)
    {
      // state 0 is when we want to skip white-spaces. 
      if ( state==0 )
      {
        if ( Character.isWhitespace(src.charAt(j)) )
          continue;
        else
        {
          numberField++;
          state=1;
        }
      } 

      // state 1 is when we want to skip non-white-spaces. */
      if ( state==1 )
      {
        if ( !Character.isWhitespace(src.charAt(j)) )
          continue;
        else
          state=0;
      }
    }
    return numberField;
  }

  //-----------------------------------------------------------------------
  /**Get the nth field in a string.
   *
   * @param source -- String containing many fields.
   * @param nth -- Specifying which field to get.
   * @return  On success, will return the nth field as a String.
   *      On failure, will return null. */
  //-----------------------------------------------------------------------
  public static String getStrField(String src, int nth)
  {
    int length=src.length();
    int state=0;
    int numberField=0;
    String target=null;
    int j,k;
    
    // Field number must start at 1. Exit this function if not. 
    if (nth<=0)
      return null;

    for(j=0; j<length; j++)
    {
      // state 0 is when we want to skip white-spaces.
      if ( state==0 ){
        if ( Character.isWhitespace(src.charAt(j)) )
          continue;
        else
        {
          state=1;

          // Got the field, switching to finished state. 
          if (++numberField >= nth)
          {
            target=src.substring(j,length);
            state=2;
          }
        }
      }

      // state 1 is when we want to skip non-white-spaces. 
      if ( state==1 )
      {
        if (!Character.isWhitespace(src.charAt(j)))
          continue;
        else
          state=0;
      }

      // state 2 is when we have found the field and will finish up
      if ( state==2 )
      {
        // Go to next space character 
        for(k=0; k<target.length(); k++)
        {
          if ( Character.isWhitespace(target.charAt(k)) )
            break;
        }
        target=target.substring(0,k);
        break;
      }
    }

    return target;
  }

  //-----------------------------------------------------------------------
  /**Extract the extension of a file.
   *
   * @param src -- String that we are going to extract the file's 
   *          extension.
   * @return  On success, it'll return a String representing a file's extension.
   *      On failure, it'll return null. */
  //-----------------------------------------------------------------------
  public static String getExtension(String src)
  {
    int i=src.lastIndexOf(".");
    if (i==-1) return null;

    return src.substring(i+1,src.length());
  }
  
  //-----------------------------------------------------------------------
  /**Extract the name of the file without the extension.
   *
   * @param src -- String that we are going to extract the file name
   *          without it's extension.
   * @return  On success, it'll return a String representing a file without 
   *      the extension.
   *      On failure, it'll return null. */
  //-----------------------------------------------------------------------
  public static String getPreExtension(String src)
  {
    int i=src.lastIndexOf(".");
    if (i==-1) return null;

    return src.substring(0,i);
  } 

  //-----------------------------------------------------------------------
  /**Same as trim() from String class except this will only trim from the
   * left side of the string.
   *
   * @param str -- The string to trim.
   * @return  Will return a trimmed string. */
  //-----------------------------------------------------------------------
  public static String trimLeft(String str)
  {
    int st=0;
    while ( st < str.length() && str.charAt(st) <= ' ' )
      st++;

    return (st > 0) ? str.substring(st,str.length()):str;

  }

  //-----------------------------------------------------------------------
  /**Same as trim() from String class except this will only trim from the
   * right side of the string.
   *
   * @param str -- The string to trim.
   * @return  Will return a trimmed string. */
  //-----------------------------------------------------------------------
  public static String trimRight(String str)
  {
    int len=str.length();

    while ( len > 0 && str.charAt(len-1) <= ' ')
      len--;

    return (len!=str.length()) ? str.substring(0,len):str;
  }

  //-----------------------------------------------------------------------
  /**Generate a string with the current date and time.
   *
   * @return A string with the current date and time. */
  //-----------------------------------------------------------------------
  public static String timeStamp()
  {
    Date d=new Date();
    SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");

    return sdf.format(d);
  }

  //-------------------------------------------------------------------------
  /**Add leading zeros if a substring in a string doesn't fill up a 
   * specifed amount of space.
   * 
   * @param stNumber  A string containing a number.
   * @param nHolder   The # of spaces we want the number to occupy.
   * @return  A string with leading zero to occupy empty spaces. */
  //-------------------------------------------------------------------------
  public static String addLeadingZero(String stNumber, int nHolder)
  {
    return addLeadingChar(stNumber, nHolder, '0');
  }

  //-------------------------------------------------------------------------
  /**Add leading characters if a substring in a string doesn't fill up a 
   * specifed amount of space.
   * 
   * @param stNumber  A string.
   * @param nHolder   The # of spaces we want the string to occupy.
   * @return  A string with leading character to occupy empty spaces. */
  //-------------------------------------------------------------------------
  public static String addLeadingChar(String stStrLine, int nHolder,
                                      char cLetter)
  {
    int nLength=stStrLine.length();
    if (nLength >= nHolder)
      return stStrLine;

    int nTotalLeadingChar=nHolder - nLength;
    StringBuffer sbBuffer=new StringBuffer(nHolder);
    for(int i=0; i<nTotalLeadingChar; i++)
      sbBuffer.append(cLetter);

    sbBuffer.append(stStrLine);
    
    return sbBuffer.toString();
  }

  //-------------------------------------------------------------------------
  /**Replace a character in a string with a substring.
   *
   * @param sbBuffer  The string we want to do replacement.
   * @param cLetter   The character to be replaced.
   * @param sbSub     The substring to replace with.
   * @return  The # of characters that got replaced. */
  //-------------------------------------------------------------------------
  public static int replace(StringBuffer sbBuffer, char cLetter, String stSub)
  {
    String stStrLine=sbBuffer.toString();
    int nCount=charCount(stStrLine, cLetter);
    if (nCount <= 0)
      return 0;

    int nLength=stStrLine.length();
    int nSubLength=stSub.length();

    sbBuffer.delete(0,nLength);
    sbBuffer.ensureCapacity(nLength+nCount*nSubLength-nCount+1);

    int i, j=0;

    for (i=0; i<nLength; i++)
    {
      if (stStrLine.charAt(i) == cLetter)
        sbBuffer.append(stSub);
      else
        sbBuffer.append(stStrLine.charAt(i));
    }

    return nCount;
  }

  public static int replace(StringBuffer sbBuffer, String stThis, String stThat)
  {
    PatternMatcher pattern=new PatternMatcher();
    pattern.setStringPattern(stThis);

    int nLength=sbBuffer.length();
    int nTotalFound=0;
    int i=0;

    String stLine=sbBuffer.toString();
    // Find how many patterns of 'oldStr' is found.
    for(i=0; i<nLength; i++)
    {
      if (pattern.read(stLine.charAt(i)))
        nTotalFound++;
    }

    // Exit if there is no pattern to replace.
    if (nTotalFound<=0)
      return 0;

    int nThisLength=stThis.length();
    int nThatLength=stThat.length();
    int nIndex=0;

    pattern.reset();
    sbBuffer.delete(0,sbBuffer.length());
    for(i=0; i<nLength; i++)
    {
      if (pattern.read(stLine.charAt(i)))
      {
        int nBufferLength=sbBuffer.length();
        sbBuffer.delete(nBufferLength-pattern.length()+1,nBufferLength);
        sbBuffer.append(stThat);
      }
      else
        sbBuffer.append(stLine.charAt(i));
    }

    return nTotalFound;
  }

  //-------------------------------------------------------------------------
  /**Counts the number of characters found in a string.
   *
   * @param stStrLine The string we want to count # of characters.
   * @param cLetter   The character we want to count.
   * @return  The number of occurance of a character in a string. */
  //-------------------------------------------------------------------------
  public static int charCount(String stStrLine, char cLetter)
  {
    int nLength=stStrLine.length(); 
    int nNumber=0;

    for(int i=0; i<nLength; i++)
      if (stStrLine.charAt(i)==cLetter) nNumber++;

    return nNumber;
  }

  //-------------------------------------------------------------------------
  /**Ensure that a directory path will ends will a slash.  If the directory
   * path already ends with a slash, then do nothing.
   *
   * @param path  The directory path.
   * @return  A directory path with a slash at the end. */
  //-------------------------------------------------------------------------
  public static String ensureDirPath(String path)
  {
    return ensureDirPath(path, File.separatorChar);
  }

  //-------------------------------------------------------------------------
  /**Ensure that a directory path will ends will a slash.  If the directory
   * path already ends with a slash, then do nothing.
   *
   * @param path  The directory path.
   * @param separator The separator character to use.
   * @return  A directory path with a slash at the end. */
  //-------------------------------------------------------------------------
  public static String ensureDirPath(String path, char separator)
  {
    if (path == null)
      return null;

    int length = path.length();
    if (length <= 0 || path.charAt(length-1) == separator)
      return path;

    return path + separator;
  }

  public static String ensureBlockLen(String str, int nMax)
  {
    int nLength = str.length();
    int nMod = nLength % nMax;
    if (nMod == 0)
      return str;

    int nRemainder = nMax - nMod;
    StringBuffer buffer = new StringBuffer(nLength+nRemainder);
    buffer.append(str);
    for(int i=0; i<nRemainder; i++)
      buffer.append(" ");

    return buffer.toString();
  }

  public static byte[] pack(String stData)
  {
    // 66 C5 6B F9 73 1F 6C BA
    // 66 5c b6 9f 37 f1 c6 ab
    int nDataLength = stData.length();
    boolean bIsOdd = (nDataLength % 2) != 0;
    int nByteDataLength = 0;
    if (bIsOdd)
    {
      nDataLength--;
      nByteDataLength = 1;
    }
    nByteDataLength += nDataLength / 2;
    byte[] byteData = new byte[nByteDataLength];
    int nIndex = 0;

    for(int i=0; i<nDataLength; i+=2)
    {
      byte low = Byte.parseByte(stData.substring(i,i+1),16);
      byte high = Byte.parseByte(stData.substring(i+1,i+2),16);
      byteData[nIndex++] = (byte)(low | (byte)(high << 4)); 
    }

    if (bIsOdd)
    {
      byteData[nIndex++] = Byte.parseByte(stData.substring(nDataLength),16);
    }

    return byteData;
  }

  public static String unpack(byte[] dataArray)
  {
    return unpack(dataArray, true);
  }

  public static String unpack(byte[] dataArray, boolean isBigEndien)
  {
    StringBuffer buffer = new StringBuffer(dataArray.length*2);
    for(int i=0; i<dataArray.length; i++)
    {
      byte data = dataArray[i]; 
      byte low = (byte)(data & 0xf); 
      byte high = (byte)(data >> 4);

      if (isBigEndien)
      {
        buffer.append(Integer.toHexString((int)low & 0xf));
        buffer.append(Integer.toHexString((int)high & 0xf)); 
      }
      else
      {
        buffer.append(Integer.toHexString((int)high & 0xf)); 
        buffer.append(Integer.toHexString((int)low & 0xf));
      }
    }

    return buffer.toString();
  }

  public static String toUpperCaseFirst(String str)
  {
    if (str == null || str.length() < 1)
      return str;

    if (str.length() == 1)
      return String.valueOf(Character.toUpperCase(str.charAt(0)));

    return Character.toUpperCase(str.charAt(0)) + str.substring(1);
  }
}
