package gov.nih.nci.cagrid.portal.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import gov.nih.nci.cagrid.portal.util.QueryInstanceCleaner;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class RunQueryInstanceCleaner {

    ApplicationContext ctx;

    public RunQueryInstanceCleaner() {
        ctx = new ClassPathXmlApplicationContext(
                new String[]{
                "classpath:applicationContext-aggr-utils.xml",
                "classpath:applicationContext-service.xml",
                "classpath:applicationContext-db.xml"


        });
    }

    public static void main(String[] args) {
        RunQueryInstanceCleaner util = new RunQueryInstanceCleaner();
        util.run();


    }

    private void run() {
        QueryInstanceCleaner cleaner = (QueryInstanceCleaner) ctx.getBean("queryInstanceCleaner");
        cleaner.clean();
    }
}