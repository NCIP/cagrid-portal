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
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class StatusChangeArchiver   {


    private GridServiceDao gridServiceDao;
    private static final Log logger = LogFactory.getLog(StatusChangeArchiver.class);
    private int maxDowntimeHours;

    public void doArchive() throws Exception {
        logger.debug("Archiving Status History");
        for(GridService service:gridServiceDao.getAll()){
            List<StatusChange> scs = service.getStatusHistory();

            for (int i = scs.size() - 1; i >= 0; i--) {
                if (ServiceStatus.ACTIVE.equals(scs.get(i).getStatus())) {
                    //keep the last active status and retire all the previous status history
                    for(int j=0;j<i;j++){
                        service.getStatusHistory().get(j).setArchived(true);
                    }
                    break;
                }
            }
            gridServiceDao.save(service);
        }
    }

    private boolean shouldArchive(StatusChange sc) {
        boolean shouldArchive = false;
        Date now = new Date();
        int diffInHours = (int) ((now
                .getTime() - sc.getTime().getTime()) / (1000 * 60 * 60));
        if (diffInHours > getMaxDowntimeHours()) {
            shouldArchive = true;
        }
        return shouldArchive;
    }


    public int getMaxDowntimeHours() {
        return maxDowntimeHours;
    }

    public void setMaxDowntimeHours(int maxDowntimeHours) {
        this.maxDowntimeHours = maxDowntimeHours;
    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

}
