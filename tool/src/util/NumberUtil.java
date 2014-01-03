/*---------------------------------------------------------------------------
| $Id: NumberUtil.java,v 1.13 2013/02/24 12:50:40 quoc Exp $ 
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//==========================================================================
/**This class contains various number manipulation methods. */
//==========================================================================
public class NumberUtil
{
  private static Pattern m_notIntegerPattern = Pattern.compile("[^0-9\\-]");
  private static Pattern m_notDecimalPattern = Pattern.compile("[^0-9\\.\\-]"); 
  private static Pattern m_endWithDot = Pattern.compile("\\.$");
  public static NumberFormat m_currencyFormat;
  public static NumberFormat m_integerFormat;
  public static NumberFormat m_percentFormat;
  public static DecimalFormat m_decimalFormat;
  public static DecimalFormat m_decimalFormatPrecise;
  public static DecimalFormat m_currencyFormatTax;

  static
  {
    m_currencyFormat = NumberFormat.getCurrencyInstance();
    m_integerFormat = NumberFormat.getIntegerInstance();
    m_percentFormat = NumberFormat.getPercentInstance();
    m_decimalFormat = new DecimalFormat("0.00");
    m_decimalFormatPrecise = new DecimalFormat("0.00000");
    m_currencyFormatTax = new DecimalFormat("###,##0.00");
    m_currencyFormatTax.setNegativePrefix("(");
    m_currencyFormatTax.setNegativeSuffix(")");
  }

  public static String toMillion(double value)
  {
    value /= 1000000;
    return m_currencyFormat.format(value);
  }

  public static String toCurrencyFormat(double value)
  {
    return m_currencyFormat.format(value);
  }

  public static String toCurrencyFormat(Object value)
  {
    return m_currencyFormat.format(value);
  }

  public static String toIntegerFormat(Object value)
  {
    return m_integerFormat.format(value);
  }

  public static String toTaxCurrency(double number)
  {
    return m_currencyFormatTax.format(number);
  }

  public static String toTaxCurrency(String numberStr)
  {
    return toTaxCurrency(Double.parseDouble(numberStr));
  }

  public static String toPercentFormat(String value)
  {
    return toPercentFormat(Double.parseDouble(value));
  }

  public static String toPercentFormat(double value)
  {
    return m_percentFormat.format(value);
  }

  public static String toDecimalFormat(Object value)
  {
    return m_decimalFormat.format(value);
  }

  public static String toPreciseDecimalFormat(Object value)
  {
    return m_decimalFormatPrecise.format(value);
  }

  public static String parseDecimal(String number)
  {
    // Get rid of non decimal characters.
    String newNumber = parseNumber(number, m_notDecimalPattern);

    // "1." will be replaced with "1.0"
    Matcher matcher = m_endWithDot.matcher(newNumber);
    if (matcher.find())
      return newNumber + "0";

    return newNumber;
  }

  public static String parseInteger(String number)
  {
    // Get rid of non integer characters.
    return parseNumber(number, m_notIntegerPattern);
  }

  private static String parseNumber(String number, Pattern pattern)
  {
    Matcher matcher = pattern.matcher(number);    
    number = matcher.replaceAll("");

    if (number.length() <= 0)
      return "0";
    return number;
  }

  //-----------------------------------------------------------------------
  /**Found out how many digits a number takes.
   *
   * @param number  The number we want to find the # of digits.
   * @return  The # of digits of 'number'. */
  //-----------------------------------------------------------------------
  public static int digitCount(int number)
  {
    int digit=0;

    if (number==0) return 1;

    if (number<0) number=-number;

    while ( number >= 1 )
    {
      number/=10;
      digit++;
    }

    return digit;
  }

  //-----------------------------------------------------------------------
  /**Calculate the # of bytes need to according to the  width and bitcount.
   *
   * @param width   The the width of an image.
   * @param bitCount  The # of bits used.
   * @return  Returns the # of bytes. */
  //-----------------------------------------------------------------------
  public static int byteWidth(int width, int bitCount)
  {
    int n=width*bitCount;
    return n/8 + ((n%8==0)?0:1);
  }

  //-----------------------------------------------------------------------
  /**Round the number up to fit multiple of blocks. This is good for 
   * calculating the actually disk space a file really takes.
   * Ex: (511,512)=512, (512,512)=512, (513,512)=1024.
   *
   * @param number  The number.
   * @param block   The block for 'number' to fit in.
   * @return The number that fit in multiple 'block'. */
  //-----------------------------------------------------------------------
  public static long upperNumber(int number, int block)
  {
    if (number<=0) return 0;
    if (block<=0) return number;

    return number/block*block+((number%block)==0?0:1)*block;
  }


  //-----------------------------------------------------------------------
  /**Extract a digit from an integer.
   *
   * @param number  The integer.
   * @param position  The decimal place of the digit from right to left.
   *                  0 is the first digit.
   * @return On success, will return the digit. 
   *         On failure, will return 0. */
  //-----------------------------------------------------------------------
  public static int getDigit(int number, int position)
  {
    int n=number;
    while(position>0)
    {
      n/=10;
      if (n==0) return 0;
      position--;
    }

    return n % 10;
  }

  //-----------------------------------------------------------------------
  /**Delete a digit from an integer.  If digit is in the middle
   * position, it will be turned to 0.
   *
   * @param number  The number.
   * @param position  The decimal place of the digit from right to left.
   *                  0 is the first digit.
   * @return  Returns a new integer that has the specified digit deleted.*/ 
  //-----------------------------------------------------------------------
  public static long ridDigit(int number, int position)
  {
    int i;
    int digit=getDigit(number,position);

    // No change since digit is 0.
    if (digit==0) return number;

    for(i=0; i<position; i++)
      digit*=10;

    number-=digit;
    return number;    
  }

  //-----------------------------------------------------------------------
  /**Convert a byte to unsigned byte. 
   *
   * @param number  The byte to convert to an unsigned number. 
   * @return  The unsigned byte number. */
  //-----------------------------------------------------------------------
  public static int toUnsignedByte(byte number)
  {
    return number & 0xff;
  }

  //-----------------------------------------------------------------------
  /**Convert a short to unsigned short.
   *
   * @param number  The short to convert to an unsigned number. 
   * @return  The unsigned short number. */
  //-----------------------------------------------------------------------
  public static int toUnsignedShort(short number)
  {
    return number & 0xffff;
  }
  
  //-----------------------------------------------------------------------
  /** Convert an int to unsigned int.
   *
   * @param number  The int to convert to an unsigned number. 
   * @return  The unsigned int number. */
  //-----------------------------------------------------------------------
  public static long toUnsignedInt(int number)
  {
    return number & 0xffffffffL;
  }

  public static short toShort(String number)
  {
    if (number != null && number.trim().length() != 0)
    {
      try
      {
        return Short.parseShort(number); 
      }
      catch(NumberFormatException e){}
    }

    return 0;
  }

  public static int toInt(String number)
  {
    if (number != null && number.trim().length() != 0)
    {
      try
      {
        return Integer.parseInt(number); 
      }
      catch(NumberFormatException e){}
    }

    return 0;
  }


  public static long toLong(String number)
  {
    if (number != null && number.trim().length() != 0)
    {
      try
      {
        return Long.parseLong(number); 
      }
      catch(NumberFormatException e){}
    }

    return 0L;
  }

  public static float toFloat(String number)
  {
    if (number != null && number.trim().length() != 0)
    {
      try
      {
        return Float.parseFloat(number);
      }
      catch(NumberFormatException e){}
    }

    return 0.0f;  
  }

  public static double toDouble(String number)
  {
    if (number != null && number.trim().length() != 0)
    {
      try
      {
        return Double.parseDouble(number);
      }
      catch(NumberFormatException e){}
    }

    return 0.0;  
  }

  public static boolean toBoolean(String number)
  {
    if (number != null && number.trim().length() != 0)
    {
      try
      {
        return Boolean.parseBoolean(number);
      }
      catch(NumberFormatException e){}
    }

    return false;
  }
}
