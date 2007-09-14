/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.Participant;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ParticipantDao extends AbstractDao<Participant> {

	@Override
	public Class domainClass() {
		return Participant.class;
	}

}
