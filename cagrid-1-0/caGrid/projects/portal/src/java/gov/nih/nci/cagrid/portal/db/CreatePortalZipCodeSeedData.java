package gov.nih.nci.cagrid.portal.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Nov 16, 2006
 * Time: 3:35:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreatePortalZipCodeSeedData {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext-dbInit.xml", "applicationContext-utils.xml"});

        PortalDDLExecutor ddlExecutor = (PortalDDLExecutor) ctx.getBean("ddlExecutor");
        //execut the DLL
        ddlExecutor.executePopulateDBWithZipCodes();
    }


}
