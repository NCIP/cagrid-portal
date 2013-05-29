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
package gov.nih.nci.cagrid.portal.service;

import com.liferay.portal.jcr.JCRFactoryUtil;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.sender.MessageSender;
import com.liferay.portal.kernel.messaging.sender.SynchronousMessageSender;
import com.liferay.portal.kernel.scheduler.SchedulerEngine;
import com.liferay.portal.kernel.scheduler.SchedulerEngineUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.scheduler.SchedulerEngineProxy;
import com.liferay.portal.search.lucene.LuceneUtil;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.TestPropsValues;
import gov.nih.nci.cagrid.portal.util.DBLoader;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.File;

//@Suite.SuiteClasses( { UserServiceTest.class,
//CommunityServiceTest.class, PersonServiceTest.class })
/**
 *
 */

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({UserServiceTest.class,
        CommunityServiceTest.class})
public class LiferayServiceTestSuite {

    @BeforeClass
    public static void setUp() {

        File derbyDBDir = new File("./lportal");
        if (derbyDBDir.exists()) {
            deleteDir(derbyDBDir);
        }

        new DBLoader("derby", "lportal", "test/data/create-derby.sql", true);
        new DBLoader("derby", "lportal", "test/data/indexes.sql", false);

        InitUtil.initWithSpring();

        FileUtil.deltree(PropsValues.LIFERAY_HOME + "/data");

        // JCR

        try {
            JCRFactoryUtil.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lucene

        LuceneUtil.checkLuceneDir(TestPropsValues.COMPANY_ID);

        // Messaging

        MessageBus messageBus = (MessageBus) PortalBeanLocatorUtil
                .locate(MessageBus.class.getName());
        MessageSender messageSender = (MessageSender) PortalBeanLocatorUtil
                .locate(MessageSender.class.getName());
        SynchronousMessageSender synchronousMessageSender = (SynchronousMessageSender) PortalBeanLocatorUtil
                .locate(SynchronousMessageSender.class.getName());

        MessageBusUtil
                .init(messageBus, messageSender, synchronousMessageSender);

        // Scheduler

        SchedulerEngine schedulerEngine = (SchedulerEngine) PortalBeanLocatorUtil
                .locate(SchedulerEngine.class.getName());

        SchedulerEngineUtil.init(new SchedulerEngineProxy());

        try {
            schedulerEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Company

        try {
            CompanyLocalServiceUtil
                    .checkCompany(TestPropsValues.COMPANY_WEB_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
