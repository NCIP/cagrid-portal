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
package gov.nih.nci.cagrid.portal.portlet.discovery;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.LiferayUser;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 * 
 */
public class DiscoveryModel {

	private List<DiscoveryDirectory> directories;
	private List<DiscoveryResults> results = new ArrayList<DiscoveryResults>();

	private DiscoveryDirectory defaultDirectory;
	private DiscoveryDirectory selectedDirectory;
	private DiscoveryResults selectedResults;
	private GridService selectedService;
	private Participant selectedParticipant;
	private Person selectedPointOfContact;
	private XMLSchema selectedXmlSchema;

    private LiferayUser liferayUser;

    /**
	 * 
	 */
	public DiscoveryModel() {

	}

	@Required
	public List<DiscoveryDirectory> getDirectories() {
		return directories;
	}

	public void setDirectories(List<DiscoveryDirectory> directories) {
		this.directories = directories;
	}

	public DiscoveryDirectory getSelectedDirectory() {
		return selectedDirectory;
	}

	public void setSelectedDirectory(DiscoveryDirectory selectedDirectory) {
		this.selectedDirectory = selectedDirectory;
	}

	public DiscoveryResults getSelectedResults() {
		return selectedResults;
	}

	public void setSelectedResults(DiscoveryResults selectedResults) {
		this.selectedResults = selectedResults;
	}

	public List<DiscoveryResults> getResults() {
		return results;
	}

	public void setResults(List<DiscoveryResults> results) {
		this.results = results;
	}

	@Required
	public DiscoveryDirectory getDefaultDirectory() {
		return defaultDirectory;
	}

	public void setDefaultDirectory(DiscoveryDirectory defaultDirectory) {
		this.defaultDirectory = defaultDirectory;
	}

	public void selectDirectory(String dirId) {
		DiscoveryDirectory dir = getDirectory(dirId);
		if (dir == null) {
//			logger.warn("No such directory '" + dirId + "'.");
		}
		setSelectedDirectory(dir);
		setSelectedResults(null);
	}

	private DiscoveryDirectory getDirectory(String dirId) {
		DiscoveryDirectory dir = null;
		for (DiscoveryDirectory d : getDirectories()) {
			if (d.getId().equals(dirId)) {
				dir = d;
				break;
			}
		}
		return dir;
	}

	public void selectResults(String id) {
        // maybe null. But it is gracefully handled
		setSelectedResults(getResults(id));
		setSelectedDirectory(null);
	}

	private DiscoveryResults getResults(String id) {
		DiscoveryResults res = null;
		for (DiscoveryResults r : getResults()) {
			if (r.getId().equals(id)) {
				res = r;
				break;
			}
		}
		return res;
	}

	public GridService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(GridService selectedService) {
		this.selectedService = selectedService;
	}

	public Participant getSelectedParticipant() {
		return selectedParticipant;
	}

	public void setSelectedParticipant(Participant selectedParticipant) {
		this.selectedParticipant = selectedParticipant;
	}

	public Person getSelectedPointOfContact() {
		return selectedPointOfContact;
	}

	public void setSelectedPointOfContact(Person selectedPointOfContact) {
		this.selectedPointOfContact = selectedPointOfContact;
	}

	public XMLSchema getSelectedXmlSchema() {
		return selectedXmlSchema;
	}

	public void setSelectedXmlSchema(XMLSchema selectedXmlSchema) {
		this.selectedXmlSchema = selectedXmlSchema;
	}

    public LiferayUser getLiferayUser() {
        return liferayUser;
    }

    public void setLiferayUser(LiferayUser liferayUser) {
        this.liferayUser = liferayUser;
    }
}
