/*
 * Created on Aug 1, 2006
 */
package gov.nci.nih.cagrid.tests.cqlprocessors;

import gov.nci.nih.cagrid.tests.cqlprocessors.util.RandomObject;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;

public class SimpleCQLQueryProcessor
	extends CQLQueryProcessor
{
	private byte[] axisConfig;
	
	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException
	{
		try {
			ArrayList<Object> resultList = new ArrayList<Object>();
			for (int i = new Random().nextInt(10); i >= 0; i--) {
				Object obj = new RandomObject().next(
					Class.forName(cqlQuery.getTarget().getName()),
					3
				);
				resultList.add(obj);
			}
			
//			return CQLQueryResultsUtil.createQueryResults(resultList, new ByteArrayInputStream(axisConfig));
//			return CQLQueryResultsUtil.createQueryResults(resultList, ClassLoader.getSystemResourceAsStream("gov/nih/nci/cagrid/tests/client/client-config.wsdd"));
			return CQLQueryResultsUtil.createQueryResults(resultList, (InputStream) null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new QueryProcessingException(e);
		} catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
			throw new QueryProcessingException(t.getMessage());
		}
	}
	
	private static CQLObjectResult createObjectResult(Object obj, MessageContext context) {
		CQLObjectResult objectResult = new CQLObjectResult();
		objectResult.setClassname(obj.getClass().getName());
		QName objectQname = context.getTypeMapping().getTypeQName(obj.getClass());
		if (objectQname == null) {
			throw new NullPointerException("No qname found for class " + obj.getClass().getName() 
				+ ". Check your client or server-config.wsdd");
		}
		MessageElement anyElement = new MessageElement(objectQname, obj);
		objectResult.set_any(new MessageElement[] {anyElement});
		return objectResult;
	}
	
	public void initialize(Map configuration) throws InitializationException
	{
//		try {
//			InputStream is = (InputStream) configuration.get(AXIS_WSDD_CONFIG_STREAM);
//			System.out.println(AXIS_WSDD_CONFIG_STREAM + "=" + is);
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			
//			byte[] buf = new byte[1024];
//			int len = 0;
//			while ((len = is.read(buf)) != -1) os.write(buf, 0, len);
//	
//			this.axisConfig = buf;
//		} catch (IOException e) {
//			throw new InitializationException(e);
//		}
	}

	public Map getRequiredParameters()
	{
		return new HashMap(0);
	}

}
