/**
 *
 */
package gov.nih.nci.cagrid.portal.bugfixes;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class BugFixMain {
    private static final Log logger = LogFactory.getLog(BugFixMain.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        logger.debug("Fixing bug: 11997");

        new ClassPathXmlApplicationContext(new String[]{
                "classpath*:gov/nih/nci/cagrid/portal/bugfixes/applicationContext-db.xml",
                "classpath*:gov/nih/nci/cagrid/portal/bugfixes/applicationContext-aggr-geocode.xml"});

        logger.debug("Bug Fix complete.");
    }

}