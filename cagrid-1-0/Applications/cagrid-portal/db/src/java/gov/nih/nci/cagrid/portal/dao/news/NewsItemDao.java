/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.news;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class NewsItemDao extends AbstractDao<NewsItem> {

	/**
	 * 
	 */
	public NewsItemDao() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return NewsItem.class;
	}

	public List<NewsItem> getLatestNewsItems(int newsItemsLimit) {
		List<NewsItem> items = new ArrayList<NewsItem>();
		List<NewsItem> l = getHibernateTemplate().find("from NewsItem order by pubDate desc");
		for(int i = 0; i < newsItemsLimit && i < l.size(); i++){
			items.add(l.get(i));
		}
		return items;
	}

}
