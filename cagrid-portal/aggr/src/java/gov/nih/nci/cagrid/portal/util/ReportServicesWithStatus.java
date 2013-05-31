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
public class ReportServicesWithStatus {

    public static void main(String[] args) throws Exception {
        ReportServicesWithStatus util = new ReportServicesWithStatus();
        if (args.length < 1) {
            System.out.println("Usage is ReportServicesWithStatus <service status>. Eg. ReportServicesWithStatus Dormant");
        }

        System.out.println("Generating a list of services with status: " + args[0]);
        util.printServiceWithStatus(ServiceStatus.valueOf(args[0]));

    }


    private void printServiceWithStatus(final ServiceStatus status) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db.xml"});

        HibernateTemplate templ = (HibernateTemplate) ctx
                .getBean("hibernateTemplate");

        System.out.println("Willl print a list of " + status + " services");
        templ.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {

                List services = session.createQuery("from GridService").list();
                for (Iterator i = services.iterator(); i.hasNext();) {
                    GridService svc = (GridService) i.next();
                    if (svc.getCurrentStatus().equals(status))
                        System.out.println(svc.getUrl());
                }

                return null;
            }
        });

    }

}
