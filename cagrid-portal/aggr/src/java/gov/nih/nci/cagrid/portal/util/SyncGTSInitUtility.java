/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

import java.io.InputStreamReader;

/**
 * Syncs with Grid Trust Service and makes portal ready to trust secure
 * services.
 *
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class SyncGTSInitUtility {

    private static final Log logger = LogFactory
            .getLog(SyncGTSInitUtility.class);
    private org.springframework.core.io.Resource syncGTSDescriptionFile;

    private boolean synchronizeEnabled = true;

    public void synchronizeWithTrustFabric() {
        if (!isSynchronizeEnabled()) {
            logger.debug("Sychronization disabled.");
        } else {
            try {
                SyncDescription description = (SyncDescription) Utils.deserializeObject(new InputStreamReader(syncGTSDescriptionFile.getInputStream()),
                        SyncDescription.class);
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
    }

    public void setSyncGTSDescriptionFile(Resource syncGTSDescriptionFile) {
        this.syncGTSDescriptionFile = syncGTSDescriptionFile;
    }

    public boolean isSynchronizeEnabled() {
        return synchronizeEnabled;
    }

    public void setSynchronizeEnabled(boolean synchronize) {
        this.synchronizeEnabled = synchronize;
    }
}
