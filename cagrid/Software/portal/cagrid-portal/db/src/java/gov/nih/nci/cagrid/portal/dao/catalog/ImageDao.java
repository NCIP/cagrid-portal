/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Image;

/**
 * @author joshua
 *
 */
public class ImageDao extends AbstractDao<Image> {

	@Override
	public Class domainClass() {
		return Image.class;
	}

}
