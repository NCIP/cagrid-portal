/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ParticipantDirectory extends DiscoveryDirectory {

	private ParticipantDirectoryType participantDirectoryType;
	private ParticipantDao participantDao;

	/**
	 * 
	 */
	public ParticipantDirectory() {
		setType(DiscoveryType.PARTICIPANT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory#getObjects()
	 */
	@Override
	public List getObjects() {
		List objects = null;

		if (ParticipantDirectoryType.ALL.equals(getParticipantDirectoryType())) {
			objects = getParticipantDao().getAll();
		} else {
			objects = getParticipantDao().getByWorkspaceAbbreviation(
					getParticipantDirectoryType().toString());
		}

		return objects;
	}

	public ParticipantDirectoryType getParticipantDirectoryType() {
		return participantDirectoryType;
	}

	public void setParticipantDirectoryType(
			ParticipantDirectoryType participantDirectoryType) {
		this.participantDirectoryType = participantDirectoryType;
	}

	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

}
