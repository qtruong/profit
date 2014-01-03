/*---------------------------------------------------------------------------
| $Id: ReflectUtil.java,v 1.21 2012/06/02 09:33:22 quoc Exp $ 
| Copyright (c) 2000 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.ArrayList;

//==========================================================================
/**A collection of easy to use reflection methods. */
//==========================================================================
public class ReflectUtil
{
  // NOTE: Ordering of PRIMITIVE_ARRAY, PRIMITIVE_CLASS_ARRAY, PRIMITIVE_ID 
  //       is important. PRIMITIVE_ARRAY must be in alphabetical order. 
  public final static String[] PRIMITIVE_ARRAY=
  {
    "boolean","byte","char","double","float",
    "int","long","short"
  };

  public final static Class[] PRIMITIVE_CLASS_ARRAY=
  {
    boolean.class, byte.class, char.class, double.class, float.class,
    int.class, long.class, short.class
  };

  public final static char[] PRIMITIVE_ID=
  {
    'a', 'b', 'c', 'd', 'f',
    'i', 'l', 's',
  };

  //-------------------------------------------------------------------------
  /**Check if the object has the specified interface.
   *
   * @param obj The object we want to check.
   * @param name The name of the interface.
   * @return If the specified interface is implemented, return true; else
   *         return false. */
  //-------------------------------------------------------------------------
  public static boolean hasInterface(Object obj, String name)
  {
    Class[] interf=obj.getClass().getInterfaces();
    for(int j=0; j<interf.length; j++)
    {
      if (interf[j].getName().equals(name))
        return true;
    }

    return false;
  }

  //-------------------------------------------------------------------------
  /**Get the value of a member variable in an object.
   *
   * @param obj The object we want to get the member variable's value.
   * @param variable The name of the member variable.
   * @return The value of the member variable. */
  //-------------------------------------------------------------------------
  public static Object getMemberValue(Object obj, String variable)
  {
    try
    {
      Class c=obj.getClass();
      Field field=c.getDeclaredField(variable);
      field.setAccessible(true);
      return field.get(obj);
    }
    catch(NoSuchFieldException e) { throw new RuntimeException(e); }
    catch(IllegalAccessException e) { throw new RuntimeException(e); }
  }

  //-------------------------------------------------------------------------
  /**Get the data type of a specified constant.
   *
   * @param data The specified constant.
   * @return The type of the specified constant. */
  //-------------------------------------------------------------------------
  public static Class getDataType(String data)
  {
    int dotCount=0;
    int length=data.length();

    // First check if it's a 'string', 'char' or 'boolean' data type.
    if (data.charAt(0)=='"' && data.charAt(length-1)=='"')
    {
      return java.lang.String.class;
    }
    else if (data.charAt(0)=='\'' && data.charAt(length-1)=='\'' && 
             length<=4)
    {
      return char.class;
    }
    else if (data.equals("true") || data.equals("false"))
    {
      return boolean.class;
    }
    else
    {
      // Check if it's a number followed by one of the casting symbol: 
      //   'f', 'd', etc...
      for(int i=0; i<length; i++)
      {
        if (data.charAt(i)=='.')
        {
          if (++dotCount > 1)
            return null;
        }
        else if (!Character.isDigit(data.charAt(i)))
        {
          if (i == length-1)
          {
            int index=Arrays.binarySearch(PRIMITIVE_ID,data.charAt(i));
            if (index<=0 || index==2)
              return null;

            return PRIMITIVE_CLASS_ARRAY[index];
          }
          else
            return null;
        }
      }
    }

    // Since we reached here, it's assume that the string is made up of
    // numbers and at most 1 dot.
    if (dotCount==1)
    {
      if (data.charAt(0)=='.' || data.charAt(length-1)=='.')
        return null;

      return float.class;
    }

    return int.class;
  }

  //-------------------------------------------------------------------------
  /**Execute a static method with no parameter using a given string name.
   * Ex: 
   *   ReflectUtil.invokeStaticMethod("TheClass.hello()");
   *
   * @param statement The string represent the static method to execute.
   * @return An object representing the return type of the static method to
   *         to execute. */
  //-------------------------------------------------------------------------
  public static Object invokeStaticMethod(String statement)
  {
    return invokeStaticMethod(statement,null);
  }

  //-------------------------------------------------------------------------
  /**Execute a static method with one parameter using a given string name.
   * Ex: 
   *   ReflectUtil.invokeStaticMethod("TheClass.display()","Hello, World");
   *
   * @param statement The string represent the static method to execute.
   * @param arg The argument to be passed to the specified static method.
   * @return An object representing the return type of the static method to
   *         to execute. */
  //-------------------------------------------------------------------------
  public static Object invokeStaticMethod(String statement, Object arg)
  {
    if (arg==null)
      return invokeStaticMethod(statement,null,null);

    Object[] args={arg};
    return invokeStaticMethod(statement,args,null);
  }

  //-------------------------------------------------------------------------
  /**Execute a static method with more than one parameter using a given
   * string name.
   * Ex: 
   *   Object[] params=new Object[]{"First param","Second param"};
   *   ReflectUtil.invokeStaticMethod("TheClass.display()",params);
   *
   * @param statement The string represent the static method to execute.
   * @param args An array of arguments to be passed to the specified static
   *             method.
   * @param obj The object instance. For static methods, set this to null.
   * @return An object representing the return type of the static method to
   *         to execute. */
  //-------------------------------------------------------------------------
  public static Object invokeStaticMethod(String statement, Object[] args, 
                                          Object obj)
  {
    int tokenCount=0;
    boolean bParamTypeDone=false;

    MethodParser classPart=new MethodParser(statement);
    Class[] paramType=null;
    if (args!=null)
    {
      if (classPart.m_parameter!=null)
        paramType=getParameterType(classPart.m_parameter);

      if (paramType==null)
      {
        paramType=new Class[args.length];
        for(int i=0; i<args.length; i++)
          paramType[i]=args[i].getClass();
      }
    }

    try
    {
      Class theClass=Class.forName(classPart.m_methodPrefix);
                                   

      Method method=theClass.getMethod(classPart.m_methodName,paramType);
      return method.invoke(obj,args);
    }
    catch(ClassNotFoundException e){ throw new RuntimeException(e); }
    catch(NoSuchMethodException e){ throw new RuntimeException(e); }
    catch(IllegalAccessException e){ throw new RuntimeException(e); }
    catch(InvocationTargetException e){ throw new RuntimeException(e); }
  }

  //-------------------------------------------------------------------------
  /**Similar to Class.forName() except this one can handle primitive string
   * name.
   *
   * @param name Fully qualified name of the desired class or primitive. 
   * @return Class object representing the desired class or primitive. */
  //-------------------------------------------------------------------------
  public static Class forName(String name)
  {
    int index=Arrays.binarySearch(PRIMITIVE_ARRAY,name);
    if (index>0)
      return PRIMITIVE_CLASS_ARRAY[index];

    try
    {
      return Class.forName(name);
    }
    catch(ClassNotFoundException e){ throw new RuntimeException(e); }
  }

  //-------------------------------------------------------------------------
  /**Get the Method object according to the method specification provided.
   *
   * @param statement The method specification.
   * @return The Method object representing to method specification.  */
  //-------------------------------------------------------------------------
  public static Method getMethod(String statement)
  {
    MethodParser classPart=new MethodParser(statement);
    Class[] paramType=null;

    if (classPart.m_parameter!=null)
      paramType=getParameterType(classPart.m_parameter);

    try
    {
      Class theClass=Class.forName(classPart.m_methodPrefix);

      return theClass.getMethod(classPart.m_methodName,paramType);
    }
    catch(ClassNotFoundException e){ throw new RuntimeException(e); }
    catch(NoSuchMethodException e){ throw new RuntimeException(e); }
  }

  public static Object createObject(String name)
  {
    return createObject(name,null);
  }

  //------------------------------------------------------------------------
  /**Create a new object that is specified in the parameter. (This is
   * exactly the same as "Class.forName(name).newInstance()" except
   * this method take care all of those annoying exceptions and just
   * return a null if any exception occurs. It can also handle constructor
   * with constant arguments. If any argument is a variable, it will
   * return null.
   *
   * @param name The object to be created. 
   * @return The newly created object if success; else return null. */
  //------------------------------------------------------------------------
  public static Object createObject(String name, Map objectTable)
  {
    String[] methodPart=MethodParser.splitMethod(name);

    try
    {
      if (methodPart[1]!=null)
      {
        Parameter paramArray=getParameter(methodPart[1],objectTable);
        Object obj = null;
        if (objectTable != null)
          obj = objectTable.get(methodPart[0]);
        Class theObject;
        if (obj == null)
          theObject = Class.forName(methodPart[0]);
        else
          theObject = obj.getClass();
        Constructor theConstructor=theObject.getConstructor(paramArray.type);
        return theConstructor.newInstance(paramArray.data);
      }
  
      return Class.forName(methodPart[0]).newInstance();
    }
    catch(ClassNotFoundException e){ throw new RuntimeException(e); }
    catch(NoSuchMethodException e){ throw new RuntimeException(e); }
    catch(InstantiationException e){ throw new RuntimeException(e); }
    catch(IllegalAccessException e){ throw new RuntimeException(e); }
    catch(InvocationTargetException e){ throw new RuntimeException(e); }
  }

  //------------------------------------------------------------------------
  /**Return an array of parameter according the parameter specification. Ex:
   * 
   *   java.lang.String, int
   *
   * will return:
   *
   *   Class[0]=java.lang.String.class
   *   Class[1]=int.class
   *
   * @param param The parameter specification.
   * @return An array of Class representing the parameter data type. */ 
  //------------------------------------------------------------------------
  public static Class[] getParameterType(String param)
  {
    StringTokenizer paramTokenizer=new StringTokenizer(param,",");
    int tokenCount=paramTokenizer.countTokens();

    Class[] paramArray=new Class[tokenCount];

    int i=0;
    while (paramTokenizer.hasMoreTokens())
    {
      String theParam=paramTokenizer.nextToken().trim();
      paramArray[i++]=forName(theParam);
    }

    return paramArray;
  }

  public static Parameter getParameter(String param)
  {
    return getParameter(param,null);
  }

  public static Parameter getParameter(String param, Map objectTable)
  {
    return getParameter(param, objectTable, null);
  }

  //------------------------------------------------------------------------
  /**This method can be used to translate constant parameters into an 
   * array of Class and Object. 
   *
   * @param param The parameter specification.
   * @return Parameter object containing an array of Class and array of 
   *         Object. */
  //------------------------------------------------------------------------
  public static Parameter getParameter(String param, Map objectTable, 
                                        Map variableTable)
  {
    ArrayList paramList = parseParameter(param);
    int size = paramList.size();
    Parameter paramArray=new Parameter(size);

    int paramIndex=0;
    for(int i=0; i<size; i++)
    {
      String item = (String) paramList.get(i);

      // Checking for data type casting.
      Class dataType=null;
      int beginCast = item.indexOf("(");
      if (beginCast == 0)
      {
        int endCast = item.indexOf(")");
        if (endCast != -1)
        {
          beginCast++;
          String dataTypeStr=item.substring(beginCast,endCast-beginCast+1);
          dataType = forName(dataTypeStr);
          item = item.substring(endCast+1);
        }
      }
      else if (variableTable != null)
      {
        String dataTypeStr = (String) variableTable.get(item);
        if (dataTypeStr != null)
        {
          dataType = forName(dataTypeStr);
        }
      }

      Class dt = getDataType(item);
      boolean isConstant = dt != null;
      if (dataType==null)
      {
        dataType = dt;
      }

      paramArray.type[paramIndex]=dataType;

      if (isConstant)
      {
        dataType=dt;
        if (dataType==java.lang.String.class)
        {
          String strConstant = item.substring(1,item.length()-1);
          strConstant = strConstant.replaceAll("\\\\\"","\"");
          paramArray.data[paramIndex] = strConstant;
        }
        else if (dataType==boolean.class)
        {
          paramArray.data[paramIndex]=new Boolean(item);
        }
        else if (dataType==char.class)
        {
          if (item.charAt(1)=='\\')
          {
            if (item.charAt(2)=='n')
              paramArray.data[paramIndex]=new Character('\n');
            else if (item.charAt(2)=='r')
              paramArray.data[paramIndex]=new Character('\r');
            else if (item.charAt(2)=='0')
              paramArray.data[paramIndex]=new Character('\0');
            else if (item.charAt(2)=='t')
              paramArray.data[paramIndex]=new Character('\t');
            else if (item.charAt(2)=='\'')
              paramArray.data[paramIndex]=new Character('\'');
            else 
              paramArray.data[paramIndex]=null;
          }
          else
          {
            paramArray.data[paramIndex]=new Character(item.charAt(1));
          }
        }
        else if (dataType!=null)
        {
          String data;
          if (Character.isLetter(item.charAt(item.length()-1)))
            data=item.substring(0,item.length()-1);
          else
            data=item;
  
          if (dataType==byte.class)
            paramArray.data[paramIndex]=new Byte(data);
          else if (dataType==short.class)
            paramArray.data[paramIndex]=new Short(data);
          else if (dataType==int.class)
            paramArray.data[paramIndex]=new Integer(data);
          else if (dataType==long.class)
            paramArray.data[paramIndex]=new Long(data);
          else if (dataType==float.class)
            paramArray.data[paramIndex]=new Float(data);
          else if (dataType==double.class)
            paramArray.data[paramIndex]=new Double(data);
          else
            paramArray.data[paramIndex]=null;
        }
      }

      if (paramArray.data[paramIndex]==null)
      {
        if (objectTable == null)
        {
          paramArray.variable[paramIndex]=item;
        }
        else
        {
          Object obj = objectTable.get(item);
          if (obj != null)
          {
            paramArray.data[paramIndex]=obj;
          }
        }
      }
      paramIndex++;
    }

    return paramArray;
  }

  public static ArrayList parseParameter(String parameter)
  {
    ArrayList paramList = new ArrayList(8);

    String[] paramPart = parameter.split(",");
    int state = 1;
    StringBuffer buffer = new StringBuffer(64);
    for(int i=0; i<paramPart.length; i++)
    {
      String line = paramPart[i].trim();
      if (state == 1)
      {
        if (line.startsWith("\"") && !line.endsWith("\""))
        {
          buffer.append(paramPart[i]);
          state = 2;
        }
        else
        {
          paramList.add(line);
        }
      }
      else if (state == 2)
      {
        buffer.append(",").append(paramPart[i]);
        if (line.endsWith("\""))
        {
          paramList.add(buffer.toString().trim());
          state = 1;
        }
      }
    }

    return paramList;
  }
}
