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
