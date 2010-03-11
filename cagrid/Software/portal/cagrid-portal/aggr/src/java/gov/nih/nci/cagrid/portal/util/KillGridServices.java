/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class KillGridServices {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "classpath:applicationContext-db.xml" });

		HibernateTemplate templ = (HibernateTemplate) ctx
				.getBean("hibernateTemplate");

		templ.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				List services = session.createQuery("from GridService").list();
				for (Iterator i = services.iterator(); i.hasNext();) {
					GridService svc = (GridService) i.next();
					System.out.println("Deleting metadata for " + svc.getUrl());

					for (final IndexService idx : svc.getIndexServices()) {
						idx.getServices().remove(svc);
						session.update(idx);
					}
					session.delete(svc);
				}

				return null;
			}
		});

	}

}
