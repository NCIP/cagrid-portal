package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ReportBannedServices {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db.xml"});

        HibernateTemplate templ = (HibernateTemplate) ctx
                .getBean("hibernateTemplate");

        System.out.println("Willl print a list of Banned services");
        templ.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                List services = session.createQuery("from GridService").list();
                for (Iterator i = services.iterator(); i.hasNext();) {
                    GridService svc = (GridService) i.next();
                    if (svc.getCurrentStatus().equals(ServiceStatus.BANNED))
                        System.out.println(svc.getUrl());
                }

                return null;
            }
        });

    }

}
