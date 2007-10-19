/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.directory.DirectoryBean;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryResultsBean;

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
	
	void addDiscoveryResults(DiscoveryResultsBean res);
	List<DiscoveryResultsBean> getDiscoveryResults();
	void setSelectedDirectoryBean(DirectoryBean bean);
	DirectoryBean getSelectedDirectoryBean();
	DiscoveryResultsBean getDiscoveryResultsBean(String resultsId);
	
	void setSelectedGridDataServiceId(Integer id);
	Integer getSelectedGridDataServiceId();
	void setSelectedUMLClass(UMLClass klass);
	UMLClass getSelectedUMLClass();
}
