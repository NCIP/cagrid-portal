package gov.nih.nci.cagrid.portal.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CreateCatalogs {


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        new ClassPathXmlApplicationContext(new String[]{
                 "classpath:applicationContext-db-aspects.xml",
                "classpath:applicationContext-service.xml",
                "classpath:applicationContext-aggr-catalog.xml"});

    }

}
 
