package gov.nih.nci.cagrid.portal;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractPortalTest extends TestCase {

     public ApplicationContext getApplicationContext(){
        return TestDB.getApplicationContext();
    }
}
