package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/** 
 *  CoreQueryProcessor
 *  CQL Query processor implementation for SDK generated data sources
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 2, 2006 
 * @version $Id$ 
 */
public class CoreQueryProcessor extends CQLQueryProcessor {
	private ApplicationService coreService;
	
	public CoreQueryProcessor(String initString) throws InitializationException {
		super(initString);
		if (initString == null || initString.length() == 0) {
			coreService = ApplicationService.getRemoteInstance();
		} else {
			coreService = ApplicationService.getRemoteInstance(initString);
		}
	}
	

	public CQLQueryResults processQuery(Object cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		DetachedCriteria objectCriteria = ProcessorHelper.createQueryCriteria(cqlQuery);
		List targetObjects = null;
		try {
			targetObjects = coreService.query(objectCriteria, cqlQuery.getName());
		} catch (ApplicationException ex) {
			throw new QueryProcessingException("Error invoking core query method: " + ex.getMessage(), ex);
		}
		CQLQueryResults results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
}
