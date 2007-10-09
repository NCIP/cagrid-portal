/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface SharedApplicationModel {

	PortalUser getPortalUser();
	void setPortalUser(PortalUser portalUser);
	void submitCqlQuery(CQLQueryInstance instance);
	List<CQLQueryInstance> getSubmittedCqlQueries();
	void setSelectedCqlQueryInstanceId(Integer id);
	Integer getSelectedCqlQueryInstanceId();

}
