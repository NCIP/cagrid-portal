/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal2.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.DiscoveryDirectory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
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
	private PointOfContact selectedPointOfContact;

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
			throw new CaGridPortletApplicationException("No such directory '"
					+ dirId + "'.");
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
		DiscoveryResults res = getResults(id);
		if (res == null) {
			throw new CaGridPortletApplicationException("No such results '"
					+ id + "'.");
		}
		setSelectedResults(res);
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

	public PointOfContact getSelectedPointOfContact() {
		return selectedPointOfContact;
	}

	public void setSelectedPointOfContact(PointOfContact selectedPointOfContact) {
		this.selectedPointOfContact = selectedPointOfContact;
	}


}
