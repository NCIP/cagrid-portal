package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.IdPAuthentication;

public class IdPAuthenticationDao extends AbstractDao<IdPAuthentication> {

	@Override
	public Class domainClass() {
		return IdPAuthentication.class;
	}

}
