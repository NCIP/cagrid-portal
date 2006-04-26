package gov.nih.nci.cagrid.data.cql2.cacore;

import gov.nih.nci.cagrid.cqlquery2.CQLQueryType;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.cql2.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/** 
 *  CoreQueryProcessor
 *  Query processor for version 2 of the CQL Query schema
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 26, 2006 
 * @version $Id$ 
 */
public class CoreQueryProcessor extends CQLQueryProcessor {
	private ApplicationService coreService;
	
	public CoreQueryProcessor(String initString) throws InitializationException {
		super(initString);
		try {
			if (getInitString() == null || getInitString().length() == 0) {
				coreService = ApplicationService.getRemoteInstance();
			} else {
				coreService = ApplicationService.getRemoteInstance(getInitString());
			}
		} catch (Exception ex) {
			throw new InitializationException("Error initializing the caCORE ApplicationService instance: " + ex.getMessage(), ex);
		}
	}


	public CQLQueryResultsType processQuery(CQLQueryType cqlQuery) throws MalformedQueryException, Exception {
		String targetClassName = cqlQuery.getCQLQuery().getName();
		DetachedCriteria queryCriteria = ProcessorHelper.processNestedObject(cqlQuery.getCQLQuery());
		List targetObjects = coreService.query(queryCriteria, targetClassName);
		CQLQueryResultsType results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
}
