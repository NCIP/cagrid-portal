package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.exception.PortalInitializationException;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;
import gov.nih.nci.cagrid.syncgts.core.SyncGTSDefault;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.core.io.Resource;


/**
 *
 * Syncs with Grid Trust Service
 * and makes portal ready to trust
 * secure services
 *
 *
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 2, 2006
 * Time: 2:08:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyncGTSInitUtility implements InitializingBean {
    private org.springframework.core.io.Resource syncGTSDescriptionFile;

    public void afterPropertiesSet() throws PortalInitializationException {
        try {
            SyncGTSDefault.setServiceSyncDescriptionLocation(syncGTSDescriptionFile.getFile()
                                                                                   .getAbsolutePath());

            SyncDescription description = SyncGTSDefault.getSyncDescription();
            SyncGTS sync = SyncGTS.getInstance();
            System.out.println("Synching with GTS");
            sync.syncOnce(description);
        } catch (Exception e) {
            System.out.println("Error syncing" + e.getMessage());
            throw new PortalInitializationException(e);
        }

        System.out.println("Synching with GTS SUCESSFUL");
    }

    public void setSyncGTSDescriptionFile(Resource syncGTSDescriptionFile) {
        this.syncGTSDescriptionFile = syncGTSDescriptionFile;
    }
}
