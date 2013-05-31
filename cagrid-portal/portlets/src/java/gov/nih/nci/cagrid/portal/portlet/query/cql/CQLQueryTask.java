/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CQLQueryTask implements Callable {

	private static final Log logger = LogFactory.getLog(CQLQueryTask.class);

	private CQLQueryInstance instance;

	private CQLQueryInstanceListener listener;
	
	private GlobusCredential cred;

	/**
	 * 
	 */
	public CQLQueryTask(CQLQueryInstance instance,
			CQLQueryInstanceListener listener, GlobusCredential cred) {
		this.instance = instance;
		this.listener = listener;
		this.cred = cred;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	public Object call() throws Exception {
		logger.debug("Running QueryInstance:" + instance.getId());
		String out = null;
		try {
			listener.onRunning(instance);

			
			DataServiceClient client = null;
			if (cred != null) {
				logger.debug("Using credentials of " + cred.getIdentity());
				client = new DataServiceClient(instance.getDataService()
						.getUrl(), cred);
			} else {
				logger.debug("No credentials found.");
				client = new DataServiceClient(instance.getDataService()
						.getUrl());
			}
			StringReader reader = new StringReader(instance.getQuery().getXml());
			CQLQuery query = (CQLQuery) Utils.deserializeObject(reader,
					CQLQuery.class);
			CQLQueryResults result = client.query(query);
			StringWriter writer = new StringWriter();
			Utils.serializeObject(result,
					DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
			out = writer.getBuffer().toString();
			listener.onComplete(instance, out);
		} catch (Exception ex) {
			logger.debug("Error running query: " + ex.getMessage(), ex);
			listener.onError(instance, ex);
			throw ex;
		}
		return out;
	}

}
