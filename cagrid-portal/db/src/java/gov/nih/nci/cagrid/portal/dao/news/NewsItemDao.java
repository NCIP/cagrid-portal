/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
