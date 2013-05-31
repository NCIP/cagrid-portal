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
package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;

import javax.persistence.*;

@Entity
@DiscriminatorValue("tool_grid_service_endpoint")
public class GridServiceEndPointCatalogEntry extends ToolDeploymentCatalogEntry {

    public GridService about;
    public boolean data;

    @Transient
    public boolean isData() {
        GridService service = getAbout();

        if (service != null) {
            if (service instanceof GridDataService)
                return true;
        }
        return false;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @OneToOne
    @JoinColumn(name = "grid_service_id")
    public GridService getAbout() {
        return about;
    }

    public void setAbout(GridService about) {
        this.about = about;
    }
}