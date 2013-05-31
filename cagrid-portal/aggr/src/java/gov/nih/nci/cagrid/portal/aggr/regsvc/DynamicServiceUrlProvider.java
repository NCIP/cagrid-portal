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
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;

import java.util.HashSet;
import java.util.Set;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DynamicServiceUrlProvider implements ServiceUrlProvider {

	private static final Log logger = LogFactory
			.getLog(DynamicServiceUrlProvider.class);

	private boolean requireMetadataCompliance;
	private long timeout;

	/**
	 * 
	 */
	public DynamicServiceUrlProvider() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceUrlProvider#getUrls(java
	 * .lang.String)
	 */
	public Set<String> getUrls(String indexServiceUrl) {

		Set<String> urls = new HashSet<String>();

		QueryThread t = new QueryThread(indexServiceUrl,
				isRequireMetadataCompliance());
		t.start();
		try {
			t.join(getTimeout());
		} catch (InterruptedException ex) {
			throw new RuntimeException("Index query thread interrupted");
		}

		if (t.getEx() != null) {
			throw new RuntimeException("Error querying index service: "
					+ t.getEx().getMessage(), t.getEx());
		}

		if (!t.isFinished()) {
			throw new RuntimeException(
					"Index query thread timed out (timeout = " + getTimeout()
							+ ").");
		}

		EndpointReferenceType[] eprs = t.getEprs();
		if (eprs != null && eprs.length > 0) {
			for (EndpointReferenceType epr : eprs) {
				urls.add(epr.getAddress().toString());
			}

		} else {
			logger.debug("No EPRs retrieved from '" + indexServiceUrl + "'");
		}

		return urls;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public boolean isRequireMetadataCompliance() {
		return requireMetadataCompliance;
	}

	public void setRequireMetadataCompliance(boolean requireMetadataCompliance) {
		this.requireMetadataCompliance = requireMetadataCompliance;
	}

}
