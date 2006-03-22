/*
 * Created on Mar 16, 2006
 */
package gov.nih.nci.cagrid.cql2xpath;

import java.io.File;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cql.CQLQueryType;
import gov.nih.nci.cagrid.cql.Group;
import gov.nih.nci.cagrid.cql.Objects;
import gov.nih.nci.cagrid.cql.Predicate;
import gov.nih.nci.cagrid.cql.Property;
import gov.nih.nci.cagrid.cql.Target;

public class CqlXpathConverter
{
	private CqlNode[] rootNodes;
	
	public CqlXpathConverter(CqlNode[] rootNodes)
	{
		super();
		
		this.rootNodes = rootNodes;
	}
	
	public String toXpath(CQLQueryType query)
	{
		StringBuffer xpath = new StringBuffer();
		
		// add target
		Target target = query.getTarget();
		CqlNode rootNode = null;
		for (int i = 0; i < rootNodes.length; i++) {
			rootNode = rootNodes[i].findChildRecursive(target.getName());
			if (rootNode != null) break;
		}
		if (rootNode == null) {
			throw new IllegalArgumentException("target " + target.getName() + " not found in CqlNode map");
		}
		CqlNode[] rootPath = rootNode.pathFromRoot();
		xpath.append("/");
		xpath.append(CqlNode.toXpath(rootPath));
		
		// add objects
		Objects objs = target.getObjects();
		Group[] groups = target.getGroup();
		Group group = null;
		if (groups != null && groups.length > 1) {
			throw new IllegalArgumentException("more than 1 group under target not supported");
		} else if (groups != null && groups.length > 0) {
			group = groups[0];
		}
		String childXpath = toXpath(new CqlNode[] { rootNode }, objs, group);
		if (childXpath != null && ! childXpath.equals("")) {
			xpath.append("[");
			xpath.append(childXpath);
			xpath.append("]");
		}
		
		return xpath.toString();
	}
	
	/**
	 * Some problems with CQL.
	 * 1) Should only have Target, properties, and associations - not objects
	 * 2) Why does target support multiple groups and objects does not?
	 * 3) Why does objects not support multiple sub-objects?
	 * 4) Group should require the predicate (why called logical operation?) if 3) is supported
	 * 5) Why does CQLQuery have to be named
	 * 6) You should support properties underneath Target, as it does not make sense to repeat the object
	 * name of the target again unless it is strictly required 
	 */
	private String toXpath(CqlNode[] path, Objects objs, Group group)
	{
		StringBuffer xpath = new StringBuffer();

		CqlNode[] childPath = null;
		if (objs != null) {
			// find path from root to the child object
			CqlNode child = null;
			String childXpath = null;
			if (! path[path.length-1].cqlName.equals(objs.getName())) {
				child = path[path.length-1].findChild(objs.getName());
				if (child == null) {
					throw new IllegalArgumentException("object " + objs.getName() + " not found in CqlNode map");
				}
				childPath = new CqlNode[path.length];
				System.arraycopy(path, 1, childPath, 0, path.length-1);
				childPath[path.length-1] = child;
				childXpath = CqlNode.toXpath(childPath);
			} else {
				childPath = path;
				child = childPath[path.length-1];
				childXpath = ".";
			}
			
			// add properties
			Property[] props = objs.getProperty();
			for (int i = 0; i < props.length; i++) {
				Property prop = props[i];
				
				CqlNode.CqlProperty cqlProp = child.findProperty(prop.getName());
				if (cqlProp == null) {
					throw new IllegalArgumentException("property " + prop.getName() + " not found in CqlNode map for object " + objs.getName());
				}
				
				// mako hack
				StringBuffer propXpath = new StringBuffer();
				if (! childXpath.equals(".")) {
					propXpath.append(childXpath);
				}
				if (cqlProp.xmlPath != null) {
					if (propXpath.length() > 0) propXpath.append("/");
					propXpath.append(cqlProp.xmlPath);
				}
				if (propXpath.length() > 0) propXpath.append("/");
				propXpath.append(cqlProp.xmlName);
				propXpath.append("/text()");
				propXpath.append(toXpath(prop.getPredicate()));
				propXpath.append("\"");
				propXpath.append(prop.getValue());
				propXpath.append("\"");

				if (xpath.length() > 0) xpath.append(" and ");
				xpath.append(propXpath);
			}
			
			// recurse
			if (objs != null) {
				String nextXpath = toXpath(childPath, objs.getObjects(), objs.getGroup());
				if (nextXpath != null && ! nextXpath.equals("")) {
					if (xpath.length() > 0) xpath.append(" and ");
					xpath.append(nextXpath);
				}
			}
			
		}
		
		if (group != null) {
			if (childPath == null) childPath = path;
			Objects[] objsArr = group.getObjects();
			for (int i = 0; i < objsArr.length; i++) {
				String nextXpath = toXpath(path, objsArr[i], objsArr[i].getGroup());
				if (nextXpath != null && ! nextXpath.equals("")) {
					if (xpath.length() > 0) xpath.append(" and ");
					xpath.append(nextXpath);
				}
				
			}
		}
		
		return xpath.toString();
	}
	
	private String toXpath(Predicate predicate)
	{
		String value = predicate.getValue();
		if (value.equals("equal")) {
			return "=";
		} else {
			throw new IllegalArgumentException("predicate " + value + " not supported");
		}
	}
	
	public static void main(String[] args)
		throws Exception
	{
		if (args.length == 0) {
			args = new String[] { 
				"queries\\scanFeatures_map.xml",
				"queries\\scanFeatures_query1.xml" 
			};
		}
		
		CqlNodeParser parser = new CqlNodeParser();
		CqlNode[] rootNodes = parser.parse(new File(args[0]));
		
		CQLQueryType query = (CQLQueryType) Utils.deserializeDocument(args[1], CQLQueryType.class);
		
		CqlXpathConverter converter = new CqlXpathConverter(rootNodes);
		String xpath = converter.toXpath(query);
		System.out.println(xpath);
	}
}
