package gov.nih.nci.cagrid.portal.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class RunDBUpdater {


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        new ClassPathXmlApplicationContext(new String[]{
                "classpath:applicationContext-db-run.xml"});

    }

}
