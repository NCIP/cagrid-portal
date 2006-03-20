/*
 * Created on Mar 16, 2006
 */
package gov.nih.nci.cagrid.cql2xpath;

import java.io.PrintStream;
import java.util.ArrayList;

public class CqlNode
{
	public static class CqlProperty
	{
		public String cqlName;
		public String xmlName;
		public String xmlPath;
	}
	
	public String cqlName;
	public String xmlName;
	public String xmlPath;
	
	public CqlNode parent;
	public ArrayList children = new ArrayList();
	public ArrayList properties = new ArrayList();

	public void print(PrintStream out)
	{
		print(out, 0);
	}

	public void print(PrintStream out, int tabCount)
	{
		String tabs = "";
		for (int i = 0; i < tabCount; i++) tabs += "\t";
		out.println(tabs + "cqlName=" + cqlName);
		out.println(tabs + "xmlName=" + xmlName);
		out.println(tabs + "xmlPath=" + xmlPath);
		
		int propCount = properties.size();
		for (int i = 0; i < propCount; i++) {
			CqlProperty prop = (CqlProperty) properties.get(i);
			out.println(tabs + " prop cqlName=" + prop.cqlName);
			out.println(tabs + " prop xmlName=" + prop.xmlName);
			out.println(tabs + " prop xmlPath=" + prop.xmlPath);
		}

		int childCount = children.size();
		for (int i = 0; i < childCount; i++) {
			((CqlNode) children.get(i)).print(out, tabCount+1);
		}
	}
	
	public CqlNode findChildRecursive(String cqlName)
	{
		if (cqlName.equals(this.cqlName)) return this;
		int childCount = children.size();
		for (int i = 0; i < childCount; i++) {
			CqlNode node = ((CqlNode) children.get(i)).findChildRecursive(cqlName);
			if (node != null) return node;
		}
		return null;
	}
	
	public CqlNode findChild(String cqlName)
	{
		int childCount = children.size();
		for (int i = 0; i < childCount; i++) {
			CqlNode node = (CqlNode) children.get(i);
			if (cqlName.equals(node.cqlName)) return node;
		}
		return null;
	}
	
	public CqlProperty findProperty(String cqlName)
	{
		int propCount = properties.size();
		for (int i = 0; i < propCount; i++) {
			CqlProperty prop = (CqlProperty) properties.get(i);
			if (cqlName.equals(prop.cqlName)) return prop;
		}
		return null;
	}
	
	public CqlNode[] pathFromRoot()
	{
		ArrayList path = new ArrayList();
		
		CqlNode node = this;
		do { path.add(0, node); } while (node.parent != null);
		
		return (CqlNode[]) path.toArray(new CqlNode[0]);
	}
	
	public CqlNode[] pathFrom(CqlNode root)
	{
		ArrayList path = new ArrayList();
		
		CqlNode node = this;
		while (node.parent != null) {
			path.add(0, node);
			if (node == root) break;
		}
		
		return (CqlNode[]) path.toArray(new CqlNode[0]);
	}
	
	public static String toXpath(CqlNode[] path)
	{
		StringBuffer xpath = new StringBuffer();
		
		for (int i = 0; i < path.length; i++) {
			CqlNode node = path[i];
			
			if (xpath.length() > 0) xpath.append("/");
			if (node.xmlPath != null) xpath.append(node.xmlPath);
			
			if (xpath.length() > 0) xpath.append("/");
			xpath.append(node.xmlName);
		}
		
		return xpath.toString();
	}
}
