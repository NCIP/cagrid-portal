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
