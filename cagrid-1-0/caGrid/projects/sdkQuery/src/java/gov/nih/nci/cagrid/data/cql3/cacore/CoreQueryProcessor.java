package gov.nih.nci.cagrid.data.cql3.cacore;

import gov.nih.nci.cagrid.cqlquery3.NestedObjectType;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql3.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/** 
 *  CoreQueryProcessor
 *  TODO:DOCUMENT ME
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
	

	public CQLQueryResultsType processQuery(NestedObjectType cqlQuery) 
		throws MalformedQueryException, QueryProcessingException {
		DetachedCriteria objectCriteria = ProcessorHelper.createQueryCriteria(cqlQuery);
		List targetObjects = null;
		try {
			targetObjects = coreService.query(objectCriteria, cqlQuery.getName());
		} catch (ApplicationException ex) {
			throw new QueryProcessingException("Error invoking core query method: " + ex.getMessage(), ex);
		}
		CQLQueryResultsType results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
}
