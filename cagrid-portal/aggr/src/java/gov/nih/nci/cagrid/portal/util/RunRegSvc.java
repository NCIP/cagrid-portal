/**
 *
 */
package gov.nih.nci.cagrid.portal.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class RunRegSvc {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        new ClassPathXmlApplicationContext(new String[]{
        		"classpath:applicationContext-db-relationships.xml",
        		"classpath:applicationContext-db-aspects.xml",
                "classpath:applicationContext-aggr-regsvc.xml"});

    }

}