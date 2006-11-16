package gov.nih.nci.cagrid.portal.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 24, 2006
 * Time: 12:30:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreatePortalDatabase {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext-dbInit.xml", "applicationContext-utils.xml"});

        PortalDDLExecutor ddlExecutor = (PortalDDLExecutor) ctx.getBean("ddlExecutor");
        //execut the DLL
        ddlExecutor.executePortalDDL();
    }


}
