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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class TextIndexer {

	private HibernateTemplate hibernateTemplate;

	private static final Log logger = LogFactory.getLog(TextIndexer.class);

	private List<String> queries = new ArrayList<String>();

	/**
	 * 
	 */
	public TextIndexer() {

	}

	public void indexAll() {

		FullTextSession sess = null;
		try {
			sess = Search.createFullTextSession(getHibernateTemplate()
					.getSessionFactory().openSession());
			for (String query : getQueries()) {

				List objects = null;
				try {
					logger.debug("Indexing: " + query);
					objects = sess.createQuery(query).list();
				} catch (Exception ex) {
					String msg = "Error running query '" + query + "': "
							+ ex.getMessage();
					logger.error(msg, ex);
					continue;
				}
				logger.debug("Have " + objects.size() + " objects to index");
				try {
					for (Object obj : objects) {
						AbstractDomainObject obj2 = (AbstractDomainObject)obj;
						logger.debug("Indexing " + obj2.getClass().getName() + ":" + obj2.getId());
						Transaction tx = sess.beginTransaction();
						sess.index(obj);
						tx.commit();
					}
					logger.debug("Finished.");
				} catch (Exception ex) {
					String msg = "Error indexing: " + ex.getMessage();
					logger.error(msg, ex);
					continue;
				}
				
			}

		} catch (Exception ex) {
			String msg = "Error indexing: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		} finally {
			if (sess != null) {
				try {

					sess.close();
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}
			}
		}
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public List<String> getQueries() {
		return queries;
	}

	public void setQueries(List<String> queries) {
		this.queries = queries;
	}

}
