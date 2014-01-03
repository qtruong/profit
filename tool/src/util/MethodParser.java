/*---------------------------------------------------------------------------
| $Id: MethodParser.java,v 1.11 2012/06/02 09:33:21 quoc Exp $ 
| Copyright (c) 2000 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.util.*;

public class MethodParser
{
  public String m_methodPrefix;
  public String m_methodName;
  public String m_parameter;

  public MethodParser(String statement)
  {
    parse(statement, null);
  }

  public MethodParser(String statement, Map dataTypeMap)
  {
    parse(statement, dataTypeMap);
  }

  public void parse(String statement)
  {
    parse(statement, null);
  }

  //-------------------------------------------------------------------------
  /**Split the whole method specification into the class name, method name,
   * and parameter. Ex: 
   *
   *   java.lang.String.startsWith(java.lang.String,java.lang.String)
   *
   * gets converted to:
   *
   *   MethodParser.m_methodPrefix=java.lang.String
   *   MethodParser.m_methodName=startsWith
   *   MethodParser.m_parameter=java.lang.String,java.langString
   *
   * @param statement The class specification. */
  //-------------------------------------------------------------------------
  public void parse(String statement, Map dataTypeMap)
  {
    String[] classMethod = splitClassMethod(statement);
    if (classMethod == null)
    {
      m_methodPrefix = null;
      m_methodName = null;
      m_parameter = null;
      return;
    }

    m_methodPrefix = classMethod[0];
    if (dataTypeMap != null)
    {
      String fullClassName = (String) dataTypeMap.get(classMethod[0]);
      if (fullClassName != null)
        m_methodPrefix = fullClassName;
    }

    String method = classMethod[1];

    String[] methodPart = splitMethod(method);
    m_methodName = methodPart[0];
    m_parameter = methodPart[1];
  }

  private String getFullClassName(String className, Map dataTypeMap)
  {
    if (dataTypeMap != null)
    {
      String fullClassName = (String) dataTypeMap.get(className);
      if (fullClassName != null)
        return fullClassName;
    }
    return null;
  }

  public static String[] splitClassMethod(String statement)
  {
    int paramMaker=statement.indexOf("(");
    int dotIndex=0;
    if (paramMaker==-1)
      dotIndex=statement.lastIndexOf(".");
    else
      dotIndex=statement.lastIndexOf(".",paramMaker);

    if (dotIndex == -1)
      return null;

    String[] classMethod = new String[2];
    classMethod[0]=statement.substring(0,dotIndex);
    classMethod[1]=statement.substring(dotIndex+1,statement.length());
    
    return classMethod;
  }

  //-------------------------------------------------------------------------
  /**Split the method into it's name and parameter. Ex:
   *
   *   java.lang.String.startsWith(java.lang.String,java.lang.String)
   *
   * gets converted to:
   *
   *   String[0]=java.lang.String.startsWith
   *   String[1]=java.lang.String,java.langString
   *
   * @param The method specification.
   * @return A String array containing the split component of a method. */
  //-------------------------------------------------------------------------
  public static String[] splitMethod(String method)
  {
    String[] methodPart=new String[2];

    int index=method.indexOf("(");
    if (index==-1)
    {
      methodPart[0]=method.trim();
      methodPart[1]=null;
    }
    else
    {
      methodPart[0]=method.substring(0,index).trim();
      int lastIndex=method.lastIndexOf(")");
      if (lastIndex==-1)
      {
        methodPart[1]=null;
      }
      else
      {
        methodPart[1]=method.substring(index+1,lastIndex).trim();
        if (methodPart[1].length()==0)
          methodPart[1]=null;
      }
    }

    return methodPart;
  }

  public String toString()
  {
    return "Class Name:    " + m_methodPrefix + "\n" +
           "Method Name:   " + m_methodName + "\n" +
           "Parameter:     " + m_parameter + "\n";
  }
}

