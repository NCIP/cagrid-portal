/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.CaGridParticipant;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CaGridParticipantDao extends AbstractDao<CaGridParticipant> {

	@Override
	public Class domainClass() {
		return CaGridParticipant.class;
	}

}
