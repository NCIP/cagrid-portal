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
package gov.nih.nci.cagrid.portal.portlet.query.results;

import java.text.MessageFormat;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class FQPServiceErrorInterpretor extends
		StringMatchServiceErrorInterpretor {

	/**
	 * 
	 */
	public FQPServiceErrorInterpretor() {

	}

	public String getErrorMessage(String serviceError) {
		int idx = serviceError.indexOf(getPattern());
		if (idx > -1) {
			int idx2 = serviceError.indexOf('\n', idx + 1);
			if (idx2 > -1) {
				String url = serviceError.substring(
						idx + getPattern().length(), idx2);
				return MessageFormat.format(getMessage(), new Object[] { url });
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String serviceError = "AxisFault\n"
				+ "faultCode: {http://schemas.xmlsoap.org/soap/envelope/}Server.generalException\n"
				+ "faultSubcode:\n"
				+ "faultString: Problem executing query: gov.nih.nci.cagrid.fqp.processor.exceptions.RemoteDataServiceException: Problem query data service at URL:http://141.161.25.20:8080/wsrf/services/cagrid/GridPIR\n"
				+ "faultActor:\n"
				+ "faultNode:\n"
				+ "faultDetail:\n"
				+ "{http://fqp.cagrid.nci.nih.gov/FederatedQueryProcessor/types}FederatedQueryProcessingFault:2008-06-02T17:11:03.879Zhttp://localhost:8085/wsrf5/services/cagrid/FederatedQueryResultsde2a39b0-30c6-11dd-b9ca-d8cc30f0acd62008-06-02T17:11:03.879Z\n"
				+ "at gov.nih.nci.cagrid.fqp.results.service.FederatedQueryResultsImpl.getResults(FederatedQueryResultsImpl.java:32)\n"
				+ "at g";
		FQPServiceErrorInterpretor i = new FQPServiceErrorInterpretor();
		i.setPattern("Problem query data service at URL:");
		i
				.setMessage("One of the data services ({0}) involved in your federated query has encountered and error. No further information is available.");
		System.out.println(i.getErrorMessage(serviceError));
	}

}
