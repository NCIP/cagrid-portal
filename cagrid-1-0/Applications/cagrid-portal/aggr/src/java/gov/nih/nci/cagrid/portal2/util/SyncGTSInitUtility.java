package gov.nih.nci.cagrid.portal2.util;

import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;
import gov.nih.nci.cagrid.syncgts.core.SyncGTSDefault;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;


/**
 * Syncs with Grid Trust Service
 * and makes portal ready to trust
 * secure services.
 * 
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class SyncGTSInitUtility {

	private static final Log logger = LogFactory.getLog(SyncGTSInitUtility.class);
	private org.springframework.core.io.Resource syncGTSDescriptionFile;


    public void synchronizeWithTrustFabric() {
        try {
            SyncGTSDefault.setServiceSyncDescriptionLocation(syncGTSDescriptionFile.getFile()
                    .getAbsolutePath());
            SyncDescription description = SyncGTSDefault.getSyncDescription();
            SyncGTS sync = SyncGTS.getInstance();
            logger.debug("Synching with GTS");
            sync.syncOnce(description);
        } catch (Exception ex) {
        	String msg = "Error syncing: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }

        logger.info("Synching with GTS SUCESSFUL");
    }

    public void setSyncGTSDescriptionFile(Resource syncGTSDescriptionFile) {
        this.syncGTSDescriptionFile = syncGTSDescriptionFile;
    }
}
