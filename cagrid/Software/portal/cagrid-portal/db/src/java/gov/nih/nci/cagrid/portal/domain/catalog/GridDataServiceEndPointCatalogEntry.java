package gov.nih.nci.cagrid.portal.domain.catalog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Entity
@DiscriminatorValue("tool_grid_data_service_endpoint")
public class GridDataServiceEndPointCatalogEntry extends GridServiceEndPointCatalogEntry {

}
