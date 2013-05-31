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
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import java.util.List;
import java.util.ArrayList;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AggregateTargetsCommand {
    private List<String> selected = new ArrayList<String>();
    private List<UMLClass> available = new ArrayList<UMLClass>();


    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    public List<UMLClass> getAvailable() {
        return available;
    }

    public void setAvailable(List<UMLClass> available) {
        this.available = available;
    }


}
