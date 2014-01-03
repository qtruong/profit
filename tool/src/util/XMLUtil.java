/*---------------------------------------------------------------------------
| $Id: XMLUtil.java,v 1.17 2012/06/02 09:33:23 quoc Exp $ 
| Copyright (c) 2001 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.tool.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XMLUtil
{
  public static Document parseXML(String filename)
  {
    try
    {
      InputStream is = new FileInputStream(filename);
      Document doc = parseXML(is);
      is.close();
      return doc;
    }
    catch(FileNotFoundException e){}
    catch(IOException e){ throw new RuntimeException(e); }
    return null;
  }

  public static Document parseXML(InputStream is) 
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      return db.parse(is);
    }
    catch(ParserConfigurationException e){ throw new RuntimeException(e); }
    catch(SAXException e){ throw new RuntimeException(e); }
    catch(IOException e){ throw new RuntimeException(e); }
  }

  public static Node getChild(Node node, String nodeName)
  {
    for (Node child = node.getFirstChild(); child != null; 
         child = child.getNextSibling()) 
    {
      if (child.getNodeName().equals(nodeName))
        return child;
    }
    return null;
  }

  public static void setAttribute(Element tag, String attribName,
                                  String attribValue)
  {
    tag.setAttribute(attribName, escapeXML(attribValue));
  }

  public static String getAttribute(Node child, String attribName)
  {
    return getAttribute(child, attribName, null);
  }

  public static String getAttribute(Node child, String attribName,
                                    String defaultStr)
  {
    NamedNodeMap atts = child.getAttributes();
    if (atts == null)
      return null;

    for (int i = 0; i < atts.getLength(); i++) 
    {
      Node att = atts.item(i);
      if (att.getNodeName().equals(attribName))
      {
        String s = att.getNodeValue();
        return unescapeXML(s);
      }
    }
    return defaultStr;
  }

  public static String getText(Node tag)
  {
    return getText(tag, null);
  }

  public static String getText(Node tag, String defaultStr)
  {
    for(Node child = tag.getFirstChild(); child != null;
        child = child.getNextSibling())
    {
      if (child.getNodeType() == Node.TEXT_NODE)
      {
        String s = child.getNodeValue().trim();
        return unescapeXML(s);
      }
    }

    return defaultStr;
  }

  public static void show(Node n)
  {
    PrintWriter writer = new PrintWriter(System.out);
    writeXML(n, writer);
    writer.flush();
  }

  public static void writeXMLHeader(PrintWriter writer)
  {
    writer.println("<?xml version=\"1.0\"?>");
  }

  public static void writeXML(Node n, PrintWriter writer)
  {
    for (Node child = n.getFirstChild(); child != null; 
         child = child.getNextSibling()) 
    {
      writeXMLRecurse(child, writer, 0);
    }
  }

  public static void writeXMLRecurse(Node n, PrintWriter writer, int indent)
  {
    String value=null;
    int type = n.getNodeType();
    switch (type) 
    {
    case Node.ELEMENT_NODE:
      writeIndentation(indent, writer);
      writer.print("<" + n.getNodeName()); 

      NamedNodeMap atts = n.getAttributes();
      for (int i = 0; i < atts.getLength(); i++) 
      {
        Node att = atts.item(i);
        writer.print(" "+att.getNodeName() + "=\"" + att.getNodeValue() + "\"");
      }
      writer.println(">");
      break;
    case Node.TEXT_NODE:
      value = n.getNodeValue();
      if (value != null)
      {
        value = value.trim();
        if (value.length() > 0)
        {
         writeIndentation(indent, writer);
         writer.println(escapeXML(value));
        }
      }
      break;
    }

    // Print children if any
    indent++;
    for (Node child = n.getFirstChild(); child != null; 
         child = child.getNextSibling()) 
    {
      writeXMLRecurse(child, writer, indent);
    }
    indent--;
    if (type==Node.ELEMENT_NODE)
    {
      writeIndentation(indent, writer);
      writer.println("</" + n.getNodeName() + ">");
    }
  }
  
  private static void writeIndentation(int indent, PrintWriter writer) 
  {
    for (int i = 0; i < indent; i++) 
      writer.print("  ");
  }

  public static String unescapeXML(String s)
  {
    s = s.replaceAll("&lt;", "<");
    s = s.replaceAll("&gt;", ">");
    s = s.replaceAll("&amp;", "&");
    s = s.replaceAll("&quot;" , "\"");
    s = s.replaceAll("&apos;", "'");
    return s.toString();
  }

  public static String escapeXML(String s) 
  {
    StringBuffer str = new StringBuffer();
    int len = (s != null) ? s.length() : 0;
    for (int i=0; i<len; i++) 
    {
       char ch = s.charAt(i);
       switch (ch) 
       {
       case '<': str.append("&lt;"); break;
       case '>': str.append("&gt;"); break;
       case '&': str.append("&amp;"); break;
       case '"': str.append("&quot;"); break;
       case '\'': str.append("&apos;"); break;
       default: str.append(ch);
       }
    }
    return str.toString();
  }

}
