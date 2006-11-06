package gov.nih.nci.cagrid.portal.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Executes the "ddlExcutor"
 * bean. Bridge between Ant and spring
 * (ideally to be written as a custom ANT Task)
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 16, 2006
 * Time: 9:26:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateCaBIGWorskpaceSeedData {


    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext-dbInit.xml", "applicationContext-utils.xml"});

        PortalDDLExecutor ddlExecutor = (PortalDDLExecutor) ctx.getBean("ddlExecutor");

        ddlExecutor.executePopulateDBWithWorkspaces();
        ddlExecutor.executePopulateDBWithParticipants();

    }


}
