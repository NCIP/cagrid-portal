/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.util;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class DeleteQueries {

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "classpath:applicationContext-db.xml" });

		HibernateTemplate templ = (HibernateTemplate) ctx
				.getBean("hibernateTemplate");

		templ.execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				Query q = session
						.createQuery("update SharedQueryCatalogEntry set about = null");
				q.executeUpdate();
				session.flush();

				SQLQuery sql = session
						.createSQLQuery("delete from dcql_svcs");
				sql.executeUpdate();
				session.flush();

				q = session.createQuery("delete from QueryResultData");
				q.executeUpdate();
				session.flush();

				q = session.createQuery("delete from QueryResultCell");
				q.executeUpdate();
				session.flush();

				q = session.createQuery("delete from QueryResultColumn");
				q.executeUpdate();
				session.flush();

				q = session.createQuery("delete from QueryResultRow");
				q.executeUpdate();
				session.flush();

				q = session.createQuery("delete from QueryResultTable");
				q.executeUpdate();
				session.flush();

				q = session.createQuery("delete from QueryInstance");
				q.executeUpdate();
				session.flush();

				q = session.createQuery("delete from Query");
				q.executeUpdate();
				session.flush();

				return null;
			}
		});
	}

}