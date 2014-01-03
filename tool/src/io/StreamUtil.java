/*---------------------------------------------------------------------------
| $Id: StreamUtil.java,v 1.7 2012/06/02 09:33:19 quoc Exp $ 
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

//===========================================================================
/**This class contains various methods that makes the tedious task of 
 * opening a file easier. */
//===========================================================================
public class StreamUtil
{
  //-------------------------------------------------------------------------
  /**This does nothing more then returning: 
   *   new PrintWriter(new (BufferedWriter(new FileWriter(filename))))
   * 
   * @param filename  The name of the file to open for writing. 
   * @return A PrintWriter object with the file ready for writing.
   * @throws IOException  When I/O errors occurs. */
  //-------------------------------------------------------------------------
  public static PrintWriter openPrintWriter(String filename) 
  {
    return openPrintWriter(filename, false);
  }

  public static PrintWriter openPrintWriter(String filename, boolean a) 
  {
    try
    {
      return new PrintWriter(new BufferedWriter(new FileWriter(filename, a)));
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  //-------------------------------------------------------------------------
  /**This does nothing more then returning: 
   * 
   * new PrintWriter(new (BufferedWriter(new FileWriter(filename),bufferSize)))
   * 
   * @param filename  The name of the file to open for writing. 
   * @param bufferSize Specify stream buffer size instead of using the default.
   * @return A PrintWriter object with the file ready for writing.
   * @throws IOException  When I/O errors occurs. */
  //-------------------------------------------------------------------------
  public static PrintWriter openPrintWriter(String filename, int bufferSize)
  {
    try
    {
      FileWriter fw=new FileWriter(filename);
      return new PrintWriter(new BufferedWriter(fw,bufferSize));
    }
    catch(Exception e) { throw new RuntimeException(e); }
  }

  //-------------------------------------------------------------------------
  /**Open a file for the BufferedReader object.
   *
   * @param filename  The file to open.
   * @return An open file stream for the BufferedReader. */
  //-------------------------------------------------------------------------
  public static BufferedReader openBufferedReader(String filename)
  {
    return openBufferedReader(new File(filename), 0);
  }

  //-------------------------------------------------------------------------
  /**Open a file for the BufferedReader object.
   *
   * @param filename  The file to open.
   * @param bufferSize The size of the stream buffer.
   * @return An open file stream for the BufferedReader. */
  //-------------------------------------------------------------------------
  public static BufferedReader openBufferedReader(String filename, 
                                                  int bufferSize) 
  {
    return openBufferedReader(new File(filename), bufferSize);
  }

  //-------------------------------------------------------------------------
  /**Open a file for the BufferedReader object.  
   *
   * @param file  The file to open.
   * @return An open file stream for the BufferedReader. */
  //-------------------------------------------------------------------------
  public static BufferedReader openBufferedReader(File file)
  {
    return openBufferedReader(file, 0);
  }

  //-------------------------------------------------------------------------
  /**Open a file for the BufferedReader object.
   *
   * @param file  The file to open.
   * @param bufferSize The size of the stream buffer.
   * @return An open file stream for the BufferedReader. */
  //-------------------------------------------------------------------------
  public static BufferedReader openBufferedReader(File file, 
                                                 int bufferSize) 
  {
    try
    {
      FileInputStream fileInput=new FileInputStream(file);
      InputStreamReader streamReader = new InputStreamReader(fileInput);
      BufferedReader reader = null;
      if (bufferSize > 0)
        reader = new BufferedReader(streamReader, bufferSize);
      else
        reader = new BufferedReader(streamReader);
      return reader;
    }
    catch(IOException e) { throw new RuntimeException(e); }
  }

  public static void close(BufferedReader reader)
  {
    try
    {
      if (reader != null)
        reader.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
  }

  public static void close(InputStream input)
  {
    try
    {
      if (input != null)
       input.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
  }

  public static void close(FileOutputStream output)
  {
    try
    {
      if (output != null)
        output.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
  }

  public static void close(RandomAccessFile input)
  {
    try
    {
      if (input != null)
       input.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
  }

  public static void close(PrintWriter writer)
  {
    if (writer != null)
      writer.close();
  }
}
