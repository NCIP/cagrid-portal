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