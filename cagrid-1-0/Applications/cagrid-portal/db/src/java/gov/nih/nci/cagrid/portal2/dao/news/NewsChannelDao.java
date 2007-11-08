/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao.news;

import gov.nih.nci.cagrid.portal2.dao.AbstractDao;
import gov.nih.nci.cagrid.portal2.domain.news.NewsChannel;

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
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return NewsChannel.class;
	}

}
