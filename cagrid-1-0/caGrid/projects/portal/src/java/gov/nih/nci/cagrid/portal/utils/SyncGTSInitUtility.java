package gov.nih.nci.cagrid.portal.utils;

import org.springframework.beans.factory.InitializingBean;
import gov.nih.nci.cagrid.syncgts.core.SyncGTSDefault;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.portal.exception.PortalInitializationException;

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

    private String syncGTSDescriptionFile;

    public void afterPropertiesSet() throws PortalInitializationException  {
        SyncGTSDefault.setServiceSyncDescriptionLocation(syncGTSDescriptionFile);
        try {
            SyncDescription description = SyncGTSDefault.getSyncDescription();

            SyncGTS sync = SyncGTS.getInstance();
            sync.syncOnce(description);
        } catch (Exception e) {
            throw new PortalInitializationException(e);

        }
    }

    public void setSyncGTSDescriptionFile(String syncGTSDescriptionFile) {
        this.syncGTSDescriptionFile = syncGTSDescriptionFile;
    }

}
