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
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;


public class SimpleCQLQueryProcessor extends CQLQueryProcessor {
	private byte[] axisConfig;


	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException
	{
		
		try {
			ArrayList<Object> resultList = new ArrayList<Object>();
			for (int i = new Random().nextInt(10); i >= 0; i--) {
				Object obj = new RandomObject().next(
					Class.forName(cqlQuery.getTarget().getName()),3);
				resultList.add(obj);
			}
			
			return CQLResultsCreationUtil.createObjectResults(resultList, cqlQuery.getTarget().getName(),  null);
		} catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
			throw new QueryProcessingException(t.getMessage());
		}
	}
}
