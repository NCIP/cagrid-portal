/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.SemanticMetadataMapping;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SemanticMetadataMappingDao extends AbstractDao<SemanticMetadataMapping> {

	/**
	 * 
	 */
	public SemanticMetadataMappingDao() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return SemanticMetadataMapping.class;
	}

}
