package gov.nih.nci.cagrid.portal.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;

/**
 * Utility to delete Portal Users
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DeleteUserUtil {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db.xml"});

        if(args.length>0){
            String _gridId = args[0];
            System.out.println("Will try and delete user with Grid Identity:" + _gridId);
            PortalUserDao _dao = (PortalUserDao)ctx.getBean("portalUserDao");
            PortalUser _user = new PortalUser();
            _user.setGridIdentity(_gridId);

            PortalUser _loadedUser=_dao.getByExample(_user);
            if(_loadedUser != null){
                System.out.println("Found User in Database");
                _dao.delete(_loadedUser);
                System.out.println("Successfully deleted User with Grid Identity:" +_gridId);
            }
            else{
                System.out.println("User not found in Database. Will not delete "+ _gridId);
            }

        }else{
            System.out.println("Please provide a Grid Identity as an argument");
        }


    }

}