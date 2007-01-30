package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.cqlresultset.CQLAttributeResult;
import gov.nih.nci.cagrid.cqlresultset.CQLCountResult;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.cqlresultset.TargetAttribute;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.MessageContext;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.message.MessageElement;

/** 
 *  CQLQueryResultsUtil
 *  @deprecated  Use {@link CQLResultsCreationUtil}
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 4, 2006 
 * @version $Id$ 
 */
public class CQLQueryResultsUtil {
	private static Map typeQNames = new HashMap();
	
	/**
	 * Creates a CQLQueryResults object from a list of Java Objects.  QNames for each object
	 * are determined as follows:
	 * 1) The Axis TypeDesc is consulted
	 * 2) If no QName is found, the Axis current MessageContext is queried
	 * 3) An attempt is made to reflect-load and invoke the object's getTypeDesc method
	 * 
	 * If no QName is found after these efforts have been made, 
	 * a null pointer exception is thrown
	 * 
	 * @param rawObjects
	 * 		The list of objects to place into CQLQueryResults
	 * @param targetClassname
	 * 		The classname of the objects in the list
	 * @return
	 * 		CQLQueryResults containing a list of object results
	 */
	public static CQLQueryResults createQueryResults(List rawObjects, String targetClassname) {
		return createQueryResults(rawObjects, targetClassname, (InputStream) null);
	}
	
	
	/**
	 * Creates a CQLQueryResults object from a list of Java Objects.  A message context
	 * is created and configured with the given config stream for locating QNames,
	 * serializers, and deserializers for the objects.
	 * 
	 * @param rawObjects
	 * 		The list of objects to place into CQLQueryResults
	 * @param targetClassname
	 * 		The classname of the objects in the list
	 * @param configStream
	 * 		The client or server-config.wsdd to use
	 * @return
	 * 		CQLQueryResults containing a list of object results
	 */
	public static CQLQueryResults createQueryResults(List rawObjects, String targetClassname, InputStream configStream) {
		typeQNames.clear();
		MessageContext context = null;
		if (configStream != null) {
			context = createMessageContext(configStream);
		} else {
			context = MessageContext.getCurrentContext();
		}
		CQLQueryResults results = new CQLQueryResults();
		results.setTargetClassname(targetClassname);
		CQLObjectResult[] objectResultArray = new CQLObjectResult[rawObjects.size()];
		int index = 0;
		Iterator objectIter = rawObjects.iterator();
		while (objectIter.hasNext()) {
			Object obj = objectIter.next();
			objectResultArray[index] = createObjectResult(obj, context);
			index++;
		}
		results.setObjectResult(objectResultArray);
		return results;
	}
	
	
	/**
	 * Creates a CQLQueryResults object from a list of objects with a given QName.
	 * This method assumes that all of the objects in the list are of the
	 * same type.
	 * 
	 * @param rawObjects
	 * 		The list of objects to be added to a CQLQueryResults object
	 * @param targetClassname
	 * 		The name of the target class
	 * @param qname
	 * 		The QName of the target data type
	 * @return
	 * 		CQLQueryResults containing a list of object results
	 */
	public static CQLQueryResults createQueryResults(List rawObjects, String targetClassname, QName qname) {
		CQLQueryResults results = new CQLQueryResults();
		results.setTargetClassname(targetClassname);
		CQLObjectResult[] objResults = new CQLObjectResult[rawObjects.size()];
		Iterator objIter = rawObjects.iterator();
		int index = 0;
		while (objIter.hasNext()) {
			Object o = objIter.next();
			CQLObjectResult object = new CQLObjectResult();
			MessageElement elem = new MessageElement(qname, o);
			object.set_any(new MessageElement[] {elem});
			objResults[index] = object;
			index++;
		}
		results.setObjectResult(objResults);
		return results;
	}
	
	
	/**
	 * Creates a CQLQueryResults object from the results of an attribute query.
	 * 
	 * @param rawAttribs
	 * 		The raw attributes from the query.  The list is expected to contain
	 * 		a singluar java.lang.Object, or java.lang.Object[] as an array
	 * 		with elements whose ordering follows the queryAttribs ordering 
	 * @param targetName
	 * 		The name of the target data type from which these attributes come
	 * @param attribNames
	 * 		The names of the query attributes
	 * @return
	 * 		CQLQueryResults containing attribute results
	 */
	public static CQLQueryResults createAttributeQueryResults(
		List rawAttribs, String targetName, String[] attribNames) {
		CQLQueryResults results = new CQLQueryResults();
		results.setTargetClassname(targetName);
		CQLAttributeResult[] attribResults = new CQLAttributeResult[rawAttribs.size()];
		Iterator rawAttribIter = rawAttribs.iterator();
		int index = 0;
		while (rawAttribIter.hasNext()) {
			// get the object and verify it's an array
			Object result = rawAttribIter.next();
			Object[] attribs = null;
			if (result.getClass().isArray()) {
				attribs = (Object[]) result;
			} else {
				attribs = new Object[] {result};
			}
			CQLAttributeResult attribResult = new CQLAttributeResult();
			TargetAttribute[] typeAttribs = new TargetAttribute[attribs.length];
			for (int i = 0; i < attribs.length; i++) {
				TargetAttribute typeAttrib = new TargetAttribute(
					attribNames[i], attribs[i] == null ? null : attribs[i].toString());
				typeAttribs[i] = typeAttrib;
			}
			attribResult.setAttribute(typeAttribs);
			attribResults[index] = attribResult;
			index++;
		}
		results.setAttributeResult(attribResults);
		return results;
	}
	
	
	/**
	 * Creates a CQLQueryResults object with a single count result in it.
	 * 
	 * @param count
	 * 		The count of the objects returned by the query
	 * @param targetName
	 * 		The classname of the target object from the query
	 * @return
	 * 		CQLQueryResults containing a single count result
	 */
	public static CQLQueryResults createCountQueryResults(long count, String targetName) {
		CQLQueryResults results = new CQLQueryResults();
		results.setTargetClassname(targetName);
		CQLCountResult countResult = new CQLCountResult(count);
		results.setCountResult(countResult);
		return results;
	}
	
	
	private static MessageContext createMessageContext(InputStream configStream) {
		EngineConfiguration config = new FileProvider(configStream);
		AxisClient client = new AxisClient(config);
		MessageContext context = new MessageContext(client);
		return context;
	}
	
	
	private static CQLObjectResult createObjectResult(Object obj, MessageContext context) {
		CQLObjectResult objectResult = new CQLObjectResult();
		QName objectQname = getQName(obj, context);
		if (objectQname == null) {
			throw new NullPointerException("No qname found for class " + obj.getClass().getName() 
				+ ". Check your client or server-config.wsdd");
		}
		MessageElement anyElement = new MessageElement(objectQname, obj);
		objectResult.set_any(new MessageElement[] {anyElement});
		return objectResult;
	}
	
	
	private static QName getQName(Object obj, MessageContext context) {
		Class objectClass = obj.getClass();
		// check cache
		QName objectQname = (QName) typeQNames.get(objectClass);
		if (objectQname == null) {
			// check the type description registry
			TypeDesc desc = TypeDesc.getTypeDescForClass(objectClass);
			if (desc != null) {
				objectQname = desc.getXmlType();
				if (objectQname != null) {
					typeQNames.put(objectClass, objectQname);
					return objectQname;
				}
			}
			// check the context
			objectQname = context.getTypeMapping().getTypeQName(objectClass);
			if (objectQname != null) {
				typeQNames.put(objectClass, objectQname);
				return objectQname;
			}
			// try to reflect-load the getTypeDesc method for axis beans...
			// This assumes the QName of the element is the same as the QName
			// of the element's type.  This may not always be the case.
			// TODO: Figgure out a way to handle non-axis beans			
			try { 
				Method m = objectClass.getMethod("getTypeDesc", new Class[0]); 
				m.setAccessible(true); 
				TypeDesc typeDesc = (TypeDesc) m.invoke(obj, new Object[0]); 
				objectQname = typeDesc.getXmlType();
				if (objectQname != null) {
					typeQNames.put(objectClass, objectQname);
					return objectQname;
				}
			} catch (Exception e) { 
				// oh well, we tried
				return null;
			}
		}
		return objectQname;
	}
}
