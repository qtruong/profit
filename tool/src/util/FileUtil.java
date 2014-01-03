/*---------------------------------------------------------------------------
| $Id: FileUtil.java,v 1.25 2012/06/02 09:33:20 quoc Exp $ 
| Copyright (c) 1999 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilePermission;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.qtt.tool.io.StreamUtil;

//==========================================================================
/**This contains various file manipulation methods. */
//==========================================================================
public class FileUtil
{
  public static final int UNKNOWN=0;
  public static final int WIN_BMP=1;
  public static final int PCX=2;
  public static final int GEM_IMG=3;
  public static final int MAC_PAINT=4;
  public static final int IFF_ILBM=5;
  public static final int GIF87A=6;
  public static final int GIF89A=7;
  public static final int JFIF=8;
  public static final int PBM=9;
  public static final int PGM=10;
  public static final int PPM=11;
  public static final int RAW_PBM=12;
  public static final int RAW_PGM=13;
  public static final int RAW_PPM=14;
  public static final int TIFF=15;
  public static final int ZIP=16;
  public static final int DOS_EXE=17;
  public static final int WIN16_EXE=18;
  public static final int WIN32_EXE=19;
  public static final int WIN16_DLL=20;
  public static final int WIN32_DLL=21;
  public static final int WIN_VXD=22;
  public static final int WIN_HELP=23;
  public static final int AVI=24;
  public static final int WAV=25;
  public static final int RMI=26;
  public static final int JAVA_CLASS=27;
  public static final int WIN_ICON=28;

  private static Pattern m_namePattern = Pattern.compile("[^\\w\\d\\-_\\.\\\\\\/]");

  public static String ensureNameFormat(String name)
  {
    String drive = "";

    // Preserved drive i.e c:\\xxxx
    if (name != null && name.length() >= 3 
        && Character.isLetter(name.charAt(0))
        && name.charAt(1) == ':' && name.charAt(2) == '\\')
    {
      drive = name.substring(0, 2);
      name = name.substring(2);
    }

    Matcher matcher = m_namePattern.matcher(name);
    name = matcher.replaceAll("__");
    if (name.length() <= 0)
      return "_";

    if (drive.length() > 0)
      name = drive + name;
    return name;
  }

  //-----------------------------------------------------------------------
  /**Generate a unique file name by attach a number at the end.  If the
   * file already ends with a number, the number will be incremented.
   *
   * @param filename  The file name.
   * @return A file name that doesn't exists in the file system. */
  //-----------------------------------------------------------------------
  public static String getUniqueName(String filename)
  {
    int lastNonDigit=StringUtil.lastNonDigit(filename);
     
    String baseName;
    int number;
    
    if (lastNonDigit>=0)
    {
      baseName=filename.substring(0,lastNonDigit+1);
      if (baseName.equals(filename))
        number=0;
      else
        number=Integer.parseInt(filename.substring(lastNonDigit))+1;
    }
    else
    {
      // Looks like it's all digit.
      baseName="";
      number=Integer.parseInt(filename);
    }

    String name=baseName+number;
    while (new File(name).exists())
    {
      number++;
      name=baseName+number;
    }

    return name;
  }

  //-----------------------------------------------------------------------
  /**Load all content from a text into a linked list.
   *
   * @param filename - The text file name to load to a linked list.
   * @return The LinkedList object containing all content from the text
   *         file. */
  //-----------------------------------------------------------------------
  public static LinkedList loadToLinkedList(String filename)
  {
    return loadToLinkedList(filename,false);
  }

  //-----------------------------------------------------------------------
  /**Load all content from a text into a linked list.
   *
   * @param filename - The text file name to load to a linked list.
   * @param bLineNumbef - Setting this to true will make this method add
   *                      a line number in the beginning a line when 
   *                      adding to the linked list.
   * @return The LinkedList object containing all content from the text
   *         file. */
  //-----------------------------------------------------------------------
  public static LinkedList loadToLinkedList(String filename, 
                                            boolean bLineNumber)
  {
    LinkedList fileStorage = new LinkedList();
    String line=null;
    int index=1;
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(filename));
      while ( (line=reader.readLine()) != null )
      {
        if (bLineNumber)
          fileStorage.add(index+": "+line);
        else
          fileStorage.add(line);
        index++;
      }
      reader.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
    finally{ StreamUtil.close(reader); }

    return fileStorage;
  }

  public static Object[] loadToArray(String filename)
  {
    return loadToArray(filename, false);
  }

  public static Object[] loadToArray(String filename, boolean isLineNumber)
  {
    return loadToVector(filename, isLineNumber).toArray();
  }

  //-----------------------------------------------------------------------
  /**Load all content from a text into a Vector.
   *
   * @param filename - The text file name to load to a Vector.
   * @return The Vector object containing all content from the text
   *         file. */
  //-----------------------------------------------------------------------
  public static Vector loadToVector(String filename)
  {
    return loadToVector(filename,false);
  }

  //-----------------------------------------------------------------------
  /**Load all content from a text into a Vector.
   *
   * @param filename - The text file name to load to a Vector.
   * @param bLineNumbef - Setting this to true will make this method add
   *                      a line number in the beginning a line when 
   *                      adding to the Vector.
   * @return The Vector object containing all content from the text
   *         file. */
  //-----------------------------------------------------------------------
  public static Vector loadToVector(String filename, boolean bLineNumber)
  {
    Vector fileStorage=new Vector(1024);
    String line=null;
    int index=1;
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(filename));
      while ( (line=reader.readLine()) != null )
      {
        if (bLineNumber)
          fileStorage.add(index+": "+line);
        else
          fileStorage.add(line);
        index++;
      }
      reader.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
    finally{ StreamUtil.close(reader); }

    return fileStorage;
  }

  //-----------------------------------------------------------------------
  /**Load all content from a text into a Vector.
   *
   * @param filename - The text file name to load to a Vector.
   * @return The ArrayList object containing all content from the text
   *         file. */
  //-----------------------------------------------------------------------
  public static ArrayList loadToArrayList(String filename)
  {
    return loadToArrayList(filename, false);
  }

  //-----------------------------------------------------------------------
  /**Load all content from a text into a Vector.
   *
   * @param filename - The text file name to load to a Vector.
   * @param bLineNumbef - Setting this to true will make this method add
   *                      a line number in the beginning a line when 
   *                      adding to the Vector.
   * @return The ArrayList object containing all content from the text
   *         file. */
  //-----------------------------------------------------------------------
  public static ArrayList loadToArrayList(String filename, boolean bLineNumber)
  {
    ArrayList fileStorage = new ArrayList(1024);
    String line = null;
    int index = 1;
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(filename));
      while ( (line=reader.readLine()) != null )
      {
        if (bLineNumber)
          fileStorage.add(index+": "+line);
        else
          fileStorage.add(line);
        index++;
      }
      reader.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
    finally{ StreamUtil.close(reader); }

    return fileStorage;
  }

  public static void saveArrayListToFile(String filename, ArrayList data) 
  {
    saveArrayToFile(filename, data.toArray());
  }

  public static void saveVectorToFile(String filename, Vector data) 
  {
    saveArrayToFile(filename, data.toArray());
  }


  public static void saveVectorToBinaryFile(String filename, Vector data)
  {
    saveArrayToBinaryFile(filename, data.toArray());
  }

  public static void saveArrayToBinaryFile(String filename, Object[] data)
  {
    try
    {
      FileOutputStream output = new FileOutputStream(filename);
      for(int i=0; i<data.length; i++)
      {
        byte[] buffer = (byte[]) data[i];
        output.write(buffer);
      }
      output.close();
    }
    catch(IOException e){ throw new RuntimeException(e); }
  }

  public static void saveStringToFile(String filename, CharSequence str)
  {
    Object[] data = {str};
    saveArrayToFile(filename, data);
  }

  public static void saveArrayToFile(String filename, Object[] data)
  {
    PrintWriter writer = StreamUtil.openPrintWriter(filename);
    for(int i=0; i<data.length; i++)
      writer.println(data[i]);
    writer.close();
  }

  //-----------------------------------------------------------------------
  /**To delete a file whether it's read-only or not.
   *
   * @param file The file to delete. */
  //-----------------------------------------------------------------------
  public static void superDelete(File file)
  {
    new FilePermission(file.getName(),"delete");
    file.delete();
  }

  //-----------------------------------------------------------------------
  /**To delete a file whether it's read-only or not.
   *
   * @param filename The file to delete. */
  //-----------------------------------------------------------------------
  public static void superDelete(String filename)
  {
    new FilePermission(filename,"delete");
    new File(filename).delete();
  }
  
  //-----------------------------------------------------------------------
  /**Attempt to identify what kind of file we are dealing with.
   *
   * @param fileName -- The file to identify.
   * @return  
   *  <pre>
   *      AVI         -- AVI.
   *      DOS_EXE     -- DOS executable.
   *      GEM_IMG     -- GEM Image format.
   *      GIF87A      -- GIF format without extension.
   *      GIF89A      -- GIF format with extension.
   *      IFF_ILBM    -- IFF/ILBM format.
   *      JAVA_CLASS  -- Java binary file. 
   *      MAC_PAINT   -- MacPaint format. (Not very reliable)
   *      PBM         -- PBM format.
   *      PCX         -- PCX format.
   *      PGM         -- PGM format.  
   *      PPM         -- PPM format.
   *      RAW_PBM     -- Raw PBM format.
   *      RAW_PGM     -- Raw PGM format.
   *      RAW_PPM     -- Raw PPM format.
   *      RMI         -- RMI.
   *      TIFF        -- TIFF.
   *      UNKNOWN     -- Can't identify file. 
   *      WAV         -- WAV.
   *      WIN_BMP     -- MS Windows BMP.
   *      WIN_HELP    -- Windows Help.
   *      WIN_ICON    -- Windows 95/98/NT icons.
   *      WIN_VXD     -- VxD driver.
   *      WIN16_DLL   -- 16-bits Windows DLL.
   *      WIN16_EXE   -- 16-bits Windows executable.
   *      WIN32_DLL   -- Win32 DLL.
   *      WIN32_EXE   -- Win32 executable.
   *      ZIP         -- ZIP.
   *  </pre> */
  //-----------------------------------------------------------------------
  public static int fileType(String fileName)
  {
    File f=new File(fileName);
    if (!f.isFile()) return UNKNOWN;
    if (f.length() < 64) return UNKNOWN;

    try
    {
      RandomAccessFile in=new RandomAccessFile(fileName,"r");
      byte[] buffer=new byte[64];
      in.read(buffer);
      in.close();
      switch(buffer[0])
      {
      case (byte)0x00:
        switch(buffer[1])
        {
        case 0x01:
          if (buffer[2]==0x00 && buffer[3]==0x08)
            return GEM_IMG;
          break;

        case 0x00:
          if (buffer[2]==0x01 && buffer[3]==0x00)
            return WIN_ICON;

          if (buffer[2]==0x00 && buffer[3]==0x02)
            return MAC_PAINT;
          break;
        }
        break;

      case (byte)0x0a:
        return PCX;

      case (byte)0x3f:
        if (buffer[1]==(byte)0x5f && buffer[2]==(byte)0x03 && 
          buffer[3]==(byte)0x00)
          return WIN_HELP;
        break;

      case (byte)0x42:
        if (buffer[1]==(byte)0x4d) 
          return WIN_BMP;
        break;

      case (byte)0x46: // 'F'
        if (buffer[1]=='O' && buffer[2]=='R' && buffer[3]=='M')
          return IFF_ILBM;
        break;

      case (byte)0x47: // 'G'
        if (buffer[1]=='I' && buffer[2]=='F' && 
          buffer[3]=='8' && buffer[5]=='a'){
          if (buffer[4]=='7')
            return GIF87A;
          else if (buffer[4]=='9')
            return GIF89A;
        }
        break;

      case (byte)0x49:
        if (buffer[1]==(byte)0x49 && buffer[2]==(byte)0x2a &&
            buffer[3]==(byte)0x00)
          return TIFF;
        break;


      case (byte)0x4d:
        if (buffer[1]==(byte)0x4d){
          if (buffer[2]==(byte)0x00 && buffer[3]==(byte)0x2a)
            return TIFF;
        }
        else if (buffer[1]==(byte)0x5a){
          long number=0;
          for(int i=0; i<4; i++){
            number|=(long)(buffer[60+i] & 0xff) << (i*8);
          }
      
          // It must be MSDOS if offset is still in the MZ header.
          if (number <= 64 )
            return DOS_EXE;

          // VxD is a driver. Shouldn't be having offset that
          //    is too big.
          boolean isVxD=false;
          if (number==0x80)
            isVxD=true;

          // Offset exceeded file size! This can only be MSDOS
          if (number+64 >= (new File(fileName)).length() )
            return DOS_EXE;
    
          in=new RandomAccessFile(fileName,"r");
          in.seek(number);
          in.read(buffer);
          in.close();
    
          // Windows
          if (buffer[0]==(byte)0x4E && buffer[1] == (byte)0x45){
            if (buffer[2]<=(byte)0xa && buffer[2]>0 && buffer[63]<30){
              // It's a DLL if STACK=0.
              if (buffer[18]==0 && buffer[19]==0)
                return WIN16_DLL;
              return WIN16_EXE;
            }
          }
  
          // VxD
          if (buffer[0]==(byte)0x4C && buffer[1] == (byte)0x45){
            if (isVxD && buffer[7]==0 && buffer[6]==0 &&
              buffer[5]==0 && buffer[4]==0)
            return WIN_VXD;
          }

          // Windows 95
          if (buffer[0]==(byte)0x50 && buffer[1] == (byte)0x45){
            if ((buffer[23] & (byte)0x20)>0)
              return WIN32_DLL;  // It's a DLL.
            else
              return WIN32_EXE;   // It's an executable.
          }
          return DOS_EXE;
        }
        break;

      case (byte)0x50:

        switch(buffer[1]){
        case '1': return PBM;
        case '2': return PGM;
        case '3': return PPM;
        case '4': return RAW_PBM;
        case '5': return RAW_PGM;
        case '6': return RAW_PPM;
        }
    
        if (buffer[1]==(byte)0x4b && buffer[2]==(byte)0x03 &&
          buffer[3]==(byte)0x04)
          return ZIP;
        break;
      
      case (byte)0x52:
        // RIFF format
        if (buffer[1]==(byte)0x49 && buffer[2]==(byte)0x46 &&
          buffer[3]==(byte)0x46){
          if (buffer[8]==(byte)0x41 && buffer[9]==(byte)0x56 &&
            buffer[10]==(byte)0x49)
            return AVI;
          if (buffer[8]==(byte)0x57 && buffer[9]==(byte)0x41 &&
            buffer[10]==(byte)0x56 && buffer[11]==(byte)0x45)
            return WAV;
          if (buffer[8]==(byte)0x52 && buffer[9]==(byte)0x4d &&
            buffer[10]==(byte)0x49)
            return RMI;
        }
        break;

      case (byte)0xca:
        // Java *.class file.
        if (buffer[1]==(byte)0xfe && buffer[2]==(byte)0xba && 
          buffer[3]==(byte)0xbe && buffer[4]==(byte)0x00 && 
          buffer[5]==(byte)0x03 && buffer[6]==(byte)0x00 &&
          buffer[7]==(byte)0x2d && buffer[8]==(byte)0x00)
          return JAVA_CLASS;
        break;

      case (byte)0xff:
        if ( buffer[1]==(byte)0xd8 && buffer[2]==(byte)0xff && 
          buffer[3]==(byte)0xe0 && 
          buffer[6]==(byte)0x4a && buffer[7]==(byte)0x46 &&
          buffer[8]==(byte)0x49 && buffer[9]==(byte)0x46 && 
          buffer[10]==(byte)0x00)

          return JFIF;
        break;
      }
    }
    catch(IOException e) { throw new RuntimeException(e); }

    return UNKNOWN;
  }

  public static boolean sameFile(String file1, String file2)
  {
    return sameFile(file1, file2, true);
  }

  public static boolean sameFile(String file1, String file2, boolean isBinary)
  {
    File fh1=new File(file1);
    if (!fh1.exists()) return false;

    File fh2=new File(file2);
    if (!fh2.exists()) return false;

    if (isBinary)
      return sameFileBinary(fh1, fh2);

    return sameFileText(fh1, fh2);
  }

  private static boolean sameFileText(File fh1, File fh2)
  {
    boolean isSame = true;
    try
    {
      FileInputStream fileInput=new FileInputStream(fh1);
      InputStreamReader streamReader = new InputStreamReader(fileInput);
      BufferedReader reader1 = new BufferedReader(streamReader,4096);

      fileInput=new FileInputStream(fh2);
      streamReader = new InputStreamReader(fileInput);
      BufferedReader reader2 = new BufferedReader(streamReader,4096);

      String line1, line2;
      while( (line1=reader1.readLine()) != null)
      {
        line2=reader2.readLine();
        if (!line1.equals(line2))
        {
          isSame = false;
          break; 
        }
      }

      reader1.close();
      reader2.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }

    return isSame;
  }

  //-----------------------------------------------------------------------
  /**Check if two files are an exact duplicate of each other.
   * 
   * @param file1 The name of the first file.
   * @param file2 The name of the second file.
   * @return  Returns true of the two files are the same.  
   *          Otherwise, return false. */
  //-----------------------------------------------------------------------
  private static boolean sameFileBinary(File fh1, File fh2)
  {
    if (fh1.length()!=fh2.length()) return false;
    
    boolean status=true;

    try
    {
      RandomAccessFile in1, in2;

      in1=new RandomAccessFile(fh1,"r");
      in2=new RandomAccessFile(fh2,"r");

      byte[] buffer1=new byte[1024];
      byte[] buffer2=new byte[1024];
      boolean done=false;
      int length;

      while(!done)
      {
        length=in1.read(buffer1);
        if (length<1024) done=true;

        in2.read(buffer2);

        for(int i=0; i<length; i++)
        {
          if (buffer1[i]!=buffer2[i])
          {
            done=true;
            status=false;
          }
        }
      }

      in1.close();
      in2.close();

    }
    catch(IOException e) { throw new RuntimeException(e); }

    return status;
  }

  /*-----------------------------------------------------------------------
  | DESCRIPTION:
  |   Check whether this file is a text or binary file.  Text file is 
  |   defined as any character that is in "XCharacter.isTextChar()".
  | PARAMETER: String name -- The name of the file to check.
  | RETURN: 
  |   false -- It's a binary file.
  |   true -- It's a text file!!!
  |----------------------------------------------------------------------*/
  private static boolean privateIsTextFile(String name)
  {
    try
    {
      byte[] buffer=new byte[1024];
      int bytesRead=0;

      RandomAccessFile in=new RandomAccessFile(name,"r");

      for(int i=0; i<8; i++)
      {
        bytesRead=in.read(buffer,0,1024);
        if (bytesRead==0) break;
        for(int j=0; j<bytesRead; j++)
        {
          char letter=(char)((int)buffer[j] & 0xff);
          if (!CharUtil.isText(letter) )
          {
            in.close();
            return false;
          }
        }

        // Should get out of loop if there are no more data.
        if (bytesRead < 1024)
          break;

      }
      in.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }

    return true;
  }

  //-----------------------------------------------------------------------
  /**Check whether this file is a text file.  Text file is defined as 
   * any character that is in "JTool.isTextChar()".
   * @param   name  The name of the file to check.
   * @return  false if it's not a text file.
   *          true if it's a text file!!!   */
  //-----------------------------------------------------------------------
  public static boolean isTextFile(String name)
  {
    File f=new File(name);

    if (!f.exists()) return false;
    if (!f.isFile()) return false;
    
    return privateIsTextFile(name);
  }

  //-----------------------------------------------------------------------
  /**Check whether this file is a binary file.  Binary file is defined as 
   * any character that is not in "JTool.isTextChar()".
   * @param   name  The name of the file to check.
   * @return  false if it's not a binary file.
   *          true if it's a binary file!!! */
  //-----------------------------------------------------------------------
  public static boolean isBinaryFile(String name)
  {
    File f=new File(name);

    if (!f.exists()) return false;
    if (!f.isFile()) return false;
    
    return !privateIsTextFile(name);
  }

  //-----------------------------------------------------------------------
  /**Check whether a text file has carriage return before the newline line.
   * Text files with carriage return are usually DOS text.  Text files
   * with no carriage return are UNIX text.
   * @return  false if it's a text without carriage return character (UNIX text).
   *          true if it's a text with carriage return character (DOS text).*/
  //-----------------------------------------------------------------------
  public static boolean isCRText(String name)
  {
    File f=new File(name);

    if (!f.exists()) return false;
    if (!f.isFile()) return false;

    int numLines=0;
    int carriageLines=0;

    try
    {
      byte[] buffer=new byte[1024];
      int bytesRead=0;
      char lastChar=0;
      char carriage=0;

      RandomAccessFile in=new RandomAccessFile(name,"r");

      while ( (bytesRead=in.read(buffer)) > 0 )
      {
        for(int i=0; i<bytesRead; i++)
        {
          if ((char)buffer[i]=='\n')
          {
            if (i==0)
              carriage=lastChar;
            else
              carriage=(char)buffer[i-1];

            if (carriage=='\r')
              carriageLines++;

            if (numLines++ > 50) break;
          }
        }
        lastChar=(char)buffer[bytesRead-1];
        if (numLines > 50) break;
      }
      in.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }

    return carriageLines==numLines;
  }

  //-------------------------------------------------------------------------
  /**Get the number of lines in a text file.
   * @param name  The name of the text file.
   * @return  On success, will return the number of lines in a file.
   *          On failure, will return -1. */
  //-------------------------------------------------------------------------
  public static long getLineCount(String name)
  {
    long lineCount=0;

    try
    {
      RandomAccessFile in=new RandomAccessFile(name,"r");

      while ( in.readLine() != null )
        lineCount++;

      in.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }

    return lineCount;
  }

  //-----------------------------------------------------------------------
  /**Create a unique file name according to a template given.
   * This is similar to the C function mktemp().
   *
   * @param strLine -- Contain the template for generating a unique file 
   *            name.  Template should be with a valid file name
   *            followed by X.  Ex: aXXX.
   * @return  On success, will return the file name.
   *      On failure, will return null. */
  //-----------------------------------------------------------------------
  public static String makeTemp(String strLine)
  {
    int length=strLine.length();
    int i;

    for(i=length-1; i>=0; i--)
    {
      if ( strLine.charAt(i)!='X' )
        break;
    }
    String baseFile=strLine.substring(0,i+1);

    int xLength=length-i-1;
    int number=0;
    StringBuffer numString=null;

    while(true)
    {
      numString=new StringBuffer(baseFile);
      int digitLeft=xLength-NumberUtil.digitCount(number);
      while(digitLeft > 0)
      {
        numString.append("0");
        digitLeft--;
      }
      numString.append((new Integer(number)).toString());

      if ( !(new File(numString.toString())).exists() )
        break;
      number++;
    }

    return numString.toString();
  }

  //-------------------------------------------------------------------------
  /**Convert all tab characters to space in a file.
   * @param tabStop -- The # of spaces in a tab character.
   * @return  On failure, it'll return false.
   *          On success, it'll return true. */
  //-------------------------------------------------------------------------
  public static long tab2Space(String name, int tabStop)
  {
    File f=new File(name);
    if (!f.exists()) return 0;
    if (!f.isFile()) return 0;

    String tmpFile=name+".tmp";

    String strLine=null;
    long totalTabs=0;
    try
    {
      BufferedWriter out=new BufferedWriter(new FileWriter(tmpFile));
      BufferedReader in=new BufferedReader(new FileReader(name));
      String line=null;
      
      while ( (line=in.readLine()) != null )
      {
        StringBuffer sbStrLine=new StringBuffer(line);
        totalTabs+=StringUtil.tab2Space(sbStrLine,tabStop);
        out.write(sbStrLine.toString());
        out.newLine();
      }
      out.close();
      in.close();
    } 
    catch(IOException e) { throw new RuntimeException(e); }

    if (totalTabs>0)
    {
      f.delete();
      new File(tmpFile).renameTo(f);
    }
    else
      new File(tmpFile).delete();

    return totalTabs;
  }

  //-------------------------------------------------------------------------
  /**Create a duplicate file.
   * @param target -- The name of the file we are going to copy to.
   * @return  On success, it will return true.
   *          On failure, it will return false. */
  //-------------------------------------------------------------------------
  public static boolean copy(String source, String target)
  {
    File f = new File(target);
    if (f.exists()) f.delete();

    try
    {
      byte[] buffer=new byte[1024];
      int number;

      RandomAccessFile in=new RandomAccessFile(source,"r");
      RandomAccessFile out=new RandomAccessFile(target,"rw");

      while ( (number=in.read(buffer,0,1024)) != -1 )
      {
        out.write(buffer,0,number);
      }

      out.close();
      in.close();
    } 
    catch(IOException e) { throw new RuntimeException(e); }

    return true;
  }

  //-------------------------------------------------------------------------
  /**Counts the total # of a specified a character in a file. 
   *
   * @param filename  The file name.
   * @param letter     The character we want to find out how many in a file.
   */
  //-------------------------------------------------------------------------
  public static long countChar(String filename, char letter)
  {
    return countChar(filename, (byte)letter);
  }

  //-------------------------------------------------------------------------
  /**Counts the total # of a specified a byte code in a file. 
   *
   * @param filename  The file name.
   * @param letter     The byte code we want to find out how many in a file.
   */
  //-------------------------------------------------------------------------
  public static long countChar(String filename, byte letter)
  {
    long total=0;
    InputStream in = null;
    try
    {
      in = new FileInputStream(filename);
      byte[] buffer=new byte[20480];
      long length=0;
      while ( (length=in.read(buffer)) > 0)
      {
        for(int i=0; i<length; i++)
          if (buffer[i]==letter)
            total++;
      }
      in.close();
    }
    catch(IOException e) { throw new RuntimeException(e); }
    finally{ StreamUtil.close(in); }
    return total;
  }

  public static boolean isAbsolutePath(String path)
  {
    if (path == null)
      return false;

    return (path.startsWith("/") || path.startsWith("\\") ||
            path.length() >= 3 && Character.isLetter(path.charAt(0)) &&
            path.charAt(1) == ':' && path.charAt(2) == '\\');
  }

  public static void touch(String filename)
  {
    RandomAccessFile out = null;
    try
    {
      out = new RandomAccessFile(filename,"rw");
      out.close();
    }
    catch(IOException e){ throw new RuntimeException(e); }
    finally{ StreamUtil.close(out); }
  }

  public static void ensureDirExist(String dir)
  {
    File path = new File(dir);
    if (!path.exists())
      path.mkdirs();
  }
}
