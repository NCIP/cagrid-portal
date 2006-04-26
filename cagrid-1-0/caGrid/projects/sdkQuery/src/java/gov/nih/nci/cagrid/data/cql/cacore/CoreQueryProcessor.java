package gov.nih.nci.cagrid.data.cql.cacore;

import gov.nih.nci.cagrid.cqlquery.CQLQueryType;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.Objects;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Subqueries;

/** 
 *  CoreQueryProcessor
 *  Implementation of CQL Query Processor that talks to a caCORE data source 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 31, 2006 
 * @version $Id$ 
 */
public class CoreQueryProcessor extends CQLQueryProcessor {
	private ApplicationService coreService;
	
	public CoreQueryProcessor(String initString) throws InitializationException {
		super(initString);
		if (initString == null || initString.length() == 0) {
			coreService = ApplicationService.getLocalInstance();
		} else {
			coreService = ApplicationService.getRemoteInstance(initString);
		}
	}
	
	
	public CQLQueryResultsType processQuery(CQLQueryType query) throws MalformedQueryException, Exception {
		// create target criteria
		Class targetClass = Class.forName(query.getTarget().getName());
		DetachedCriteria targetCriteria = DetachedCriteria.forClass(targetClass);
		
		// process the target's object
		if (query.getTarget().getObjects() != null) {
			Objects object = query.getTarget().getObjects();
			DetachedCriteria objectCriteria = ProcessorHelper.processObjects(object);
			Criterion objectCriterion = Subqueries.exists(objectCriteria);
			targetCriteria.add(objectCriterion);
		}

		// process the target's groups
		if (query.getTarget().getGroup() != null) {
			Group[] groups = query.getTarget().getGroup();
			for (int i = 0; i < groups.length; i++) {
				Junction groupJunction = ProcessorHelper.processGroup(groups[i]);
				targetCriteria.add(groupJunction);
			}
		}
		
		// submit the query to the Application Service
		List targetObjects = coreService.query(targetCriteria, query.getTarget().getName());
		CQLQueryResultsType results = CQLQueryResultsUtil.createQueryResults(targetObjects);
		return results;
	}
}
