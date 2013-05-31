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
package gov.nih.nci.cagrid.portal.portlet.map.ajax;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SummaryBean {

    private int participants, analyticalServices, dataServices;
    private String classCountStats ;

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public int getAnalyticalServices() {
        return analyticalServices;
    }

    public void setAnalyticalServices(int analyticalServices) {
        this.analyticalServices = analyticalServices;
    }

    public int getDataServices() {
        return dataServices;
    }

    public void setDataServices(int dataServices) {
        this.dataServices = dataServices;
    }


	public String getClassCountStats() {
		return classCountStats;
	}

	public void setClassCountStats(String classCountStats) {
		this.classCountStats = classCountStats;
	}
}
