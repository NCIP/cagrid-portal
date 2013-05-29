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

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;

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
public class KillServiceMetadata {

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
					GridService svc = (GridService)i.next();
					System.out.println("Deleting metadata for " + svc.getUrl());
					
					ServiceMetadata meta = svc.getServiceMetadata();
					svc.setServiceMetadata(null);
					session.delete(meta);
				}
				
				return null;
			}
		});

	}

}
