/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.news;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsChannel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class NewsChannelDao extends AbstractDao<NewsChannel> {

	/**
	 * 
	 */
	public NewsChannelDao() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return NewsChannel.class;
	}

}
